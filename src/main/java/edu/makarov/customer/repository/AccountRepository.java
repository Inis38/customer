package edu.makarov.customer.repository;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAccountByCustomerId(long id);

    List<Account> findAccountsByCustomer(Customer customer);
}
