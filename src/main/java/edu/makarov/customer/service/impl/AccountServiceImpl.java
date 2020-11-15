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
import java.util.Optional;

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
    public Optional<List<Account>> findAllByCustomerId(long id) {

        return customerService.findById(id)
                .map(accountRepository::findAccountsByCustomer)
                .orElse(Optional.empty());
    }

    @Override
    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> create(Account account, long customerId) {

        return customerService.findById(customerId)
                .map(customer -> {
                    account.setCustomer(customer);
                    return Optional.of(create(account));
                })
                .orElse(Optional.empty());
    }

    @Override
    public Optional<Account> update(long id, Account account) {

        return findById(id)
                .map(accountFromDb -> {
                    BeanUtils.copyProperties(account, accountFromDb, "id");
                    return Optional.of(create(accountFromDb));
                })
                .orElse(Optional.empty());
    }

    @Override
    public boolean delete(long id) {
        if (!findById(id).isPresent()) {
            return false;
        }
        accountRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Account> increaseBalance(BalanceChangeDTO balanceChangeDTO) {
        long id = balanceChangeDTO.getId();
        BigDecimal sum = balanceChangeDTO.getSum().setScale(2, BigDecimal.ROUND_DOWN);
        if (id == 0 || sum == null || sum.floatValue() < 0) {
            return Optional.empty();
        }
        return increaseBalance(id, sum);
    }

    private Optional<Account> increaseBalance(long id, BigDecimal sum) {

        return findById(id)
                .map(account -> {
                    account.setBalance(account.getBalance().add(sum));
                    return Optional.of(create(account));
                })
                .orElse(Optional.empty());
    }

    @Override
    public Optional<Account> reduceBalance(BalanceChangeDTO balanceChangeDTO) {
        long id = balanceChangeDTO.getId();
        BigDecimal sum = balanceChangeDTO.getSum().setScale(2, BigDecimal.ROUND_DOWN);
        if (id == 0 || sum == null || sum.floatValue() < 0) {
            return Optional.empty();
        }
        return reduceBalance(id, sum);
    }

    private Optional<Account> reduceBalance(long id, BigDecimal sum) {

        return (Optional<Account>) findById(id)
                .map(account -> {
                    if (account.getBalance().floatValue() < sum.floatValue()) {
                        return Optional.empty();
                    }
                    account.setBalance(account.getBalance().subtract(sum));
                    return Optional.of(create(account));
                })
                .orElse(Optional.empty());
    }

    @Override
    public boolean transferMoney(MoneyTransactionDTO transaction) {
        BigDecimal sum = transaction.getSum().setScale(2, BigDecimal.ROUND_DOWN);
        return transferMoney(transaction.getReduceId(), transaction.getIncreaseId(), sum);
    }

    private boolean transferMoney(long from, long to, BigDecimal sum) {
        Optional<Account> accountFrom = findById(from);
        Optional<Account> accountTo = findById(to);
        if (!accountFrom.isPresent() || !accountTo.isPresent()) {
            return false;
        }
        if (!reduceBalance(accountFrom.get().getId(), sum).isPresent()) {
            return false;
        }
        increaseBalance(accountTo.get().getId(), sum);
        return true;
    }
}
