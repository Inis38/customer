package edu.makarov.customer.servise;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;

import java.util.List;
import java.util.Optional;

public interface AccountService extends BaseService<Account> {

    Account create(Account account, long customerId);

    List<Account> findAllByCustomerId(long id);

}
