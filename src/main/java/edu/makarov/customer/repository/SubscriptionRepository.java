package edu.makarov.customer.repository;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<List<Subscription>> findSubscriptionsByCustomers(Customer customer);
}
