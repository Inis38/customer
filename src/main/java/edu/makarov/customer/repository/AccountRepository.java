package edu.makarov.customer.repository;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<List<Account>> findAccountsByCustomer(Customer customer);
}
