package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.repository.CustomerRepository;
import edu.makarov.customer.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(long id, Customer customer) {
        Customer customerFromDb = findById(id);
        if (customerFromDb == null) {
            return null;
        }
        BeanUtils.copyProperties(customer, customerFromDb, "id");
        return create(customerFromDb);
    }

    @Override
    public boolean delete(long id) {
        if (findById(id) == null) {
            return false;
        }
        customerRepository.deleteById(id);
        return true;
    }
}
