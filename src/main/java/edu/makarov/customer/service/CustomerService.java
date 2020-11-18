package edu.makarov.customer.service;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.models.dto.SubscriptionManagementDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CustomerService extends BaseService<Customer> {

    Optional<List<Subscription>> findSubscriptions(long customerId);

    Optional<Set<Subscription>> addSubscription(SubscriptionManagementDTO subDto);

    Optional<Set<Subscription>> deleteSubscription(SubscriptionManagementDTO subDto);

    Optional<Customer> sendCustomerToQueue(long customerId);
}
