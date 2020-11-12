package edu.makarov.customer.service;

import edu.makarov.customer.models.Account;

import java.util.List;
import java.util.Map;

public interface AccountService extends BaseService<Account> {

    Account create(Account account, long customerId);

    List<Account> findAllByCustomerId(long id);

    Account addBalance(Map<String, String> map);
}
