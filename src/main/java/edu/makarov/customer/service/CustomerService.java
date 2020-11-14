package edu.makarov.customer.service;

import edu.makarov.customer.models.Customer;

import java.util.Optional;

public interface CustomerService extends BaseService<Customer> {

    Optional<Customer> findById(long id);

    Optional<Customer> update(long id, Customer model);
}
