package edu.makarov.customer.servise.impl;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.repository.AccountRepository;
import edu.makarov.customer.servise.AccountService;
import edu.makarov.customer.servise.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerService customerService;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> findAllByCustomerId(long id) {
        return accountRepository.findAccountByCustomerId(id);
    }

    @Override
    public Account findById(long id) {
        return accountRepository.findById(id).get();
    }

    @Override
    public Account create(Account customer) {
        return accountRepository.save(customer);
    }

    @Override
    public Account create(Account account, long customerId) {
        account.setCustomer(customerService.findById(customerId));
        return create(account);
    }

    @Override
    public Account update(long id, Account account) {
        Account accountFromDb = findById(id);
        BeanUtils.copyProperties(account, accountFromDb, "id");
        return create(accountFromDb);
    }

    @Override
    public void delete(long id) {
        accountRepository.deleteById(id);
    }
}
