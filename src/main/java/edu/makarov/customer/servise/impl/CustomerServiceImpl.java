package edu.makarov.customer.servise.impl;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.repository.CustomerRepository;
import edu.makarov.customer.servise.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(long id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(long id, Customer customer) {
        Customer customerFromDb = findById(id);
        BeanUtils.copyProperties(customer, customerFromDb, "id");
        return create(customerFromDb);
    }

    @Override
    public void delete(long id) {
        customerRepository.deleteById(id);
    }
}
