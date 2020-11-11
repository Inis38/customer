package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.repository.AccountRepository;
import edu.makarov.customer.service.AccountService;
import edu.makarov.customer.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Account create(Account customer) {
        return accountRepository.save(customer);
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
}