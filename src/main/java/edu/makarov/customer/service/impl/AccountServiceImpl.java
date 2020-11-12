package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.repository.AccountRepository;
import edu.makarov.customer.service.AccountService;
import edu.makarov.customer.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
        if (customerService.findById(id) == null) {
            return null;
        }
        return accountRepository.findAccountByCustomerId(id);
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
        Customer customer = customerService.findById(customerId);
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
    public Account addBalance(Map<String, String> map) {
        Long id = Long.valueOf(map.get("id"));
        BigDecimal summ = checkValue(map.get("addBalance"));
        if (id == null || summ == null || summ.floatValue() < 0) {
            return null;
        }
        Account account = findById(id);
        account.setBalance(account.getBalance().add(summ));
        return create(account);
    }

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
