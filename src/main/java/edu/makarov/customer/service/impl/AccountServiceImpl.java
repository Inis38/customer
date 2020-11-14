package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.dto.BalanceChangeDTO;
import edu.makarov.customer.models.dto.MoneyTransactionDTO;
import edu.makarov.customer.repository.AccountRepository;
import edu.makarov.customer.service.AccountService;
import edu.makarov.customer.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, CustomerService customerService) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> findAllByCustomerId(long id) {

        return customerService.findById(id)
                .map(accountRepository::findAccountsByCustomer)
                .orElse(new ArrayList<>());
    }

    @Override
    public Account findById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account create(Account account, long customerId) {
        Customer customer = customerService.findById(customerId).orElse(null);
        if (customer == null) {
            return null;
        }
        account.setCustomer(customer);
        return create(account);
    }

    @Override
    public Account update(long id, Account account) {
        Account accountFromDb = findById(id);
        if (accountFromDb == null) {
            return null;
        }
        BeanUtils.copyProperties(account, accountFromDb, "id");
        return create(accountFromDb);
    }

    @Override
    public boolean delete(long id) {
        if (findById(id) == null) {
            return false;
        }
        accountRepository.deleteById(id);
        return true;
    }

    @Override
    public Account increaseBalance(BalanceChangeDTO balanceChangeDTO) {
        Long id = balanceChangeDTO.getId();
        BigDecimal sum = checkValue(balanceChangeDTO.getSum().toString());
        if (id == null || sum == null || sum.floatValue() < 0) {
            return null;
        }
        return increaseBalance(id, sum);
    }

    private Account increaseBalance(long id, BigDecimal sum) {
        Account account = findById(id);
        account.setBalance(account.getBalance().add(sum));
        return create(account);
    }

    @Override
    public Account reduceBalance(BalanceChangeDTO balanceChangeDTO) {
        Long id = Long.valueOf(balanceChangeDTO.getId());
        BigDecimal sum = checkValue(balanceChangeDTO.getSum().toString());
        if (id == null || sum == null || sum.floatValue() < 0) {
            return null;
        }
        return reduceBalance(id, sum);
    }

    private Account reduceBalance(long id, BigDecimal sum) {
        Account account = findById(id);
        if (account.getBalance().floatValue() < sum.floatValue()) {
            return null;
        }
        account.setBalance(account.getBalance().subtract(sum));
        return create(account);
    }

    @Override
    public boolean transferMoney(MoneyTransactionDTO transaction) {
        long from = transaction.getReduceId();
        long to = transaction.getIncreaseId();
        BigDecimal sum = transaction.getSum().setScale(2, BigDecimal.ROUND_DOWN);
        return transferMoney(from, to, sum);
    }

    private boolean transferMoney(long from, long to, BigDecimal sum) {
        Account accountFrom = findById(from);
        Account accountTo = findById(to);
        if (accountFrom == null || accountTo == null) {
            return false;
        }
        if (reduceBalance(accountFrom.getId(), sum) == null) {
            return false;
        }
        increaseBalance(accountTo.getId(), sum);
        return true;
    }

    //TODO отказаться от этого метода
    private BigDecimal checkValue(String value) {
        try {
            BigDecimal decimalValue = new BigDecimal(value);
            decimalValue = decimalValue.setScale(2, BigDecimal.ROUND_DOWN);
            return decimalValue;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
