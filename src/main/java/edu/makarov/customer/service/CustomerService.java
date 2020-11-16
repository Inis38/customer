package edu.makarov.customer.service;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;

import java.util.List;
import java.util.Optional;

public interface CustomerService extends BaseService<Customer> {

    Optional<List<Subscription>> findSubscriptions(long customerId);
}
