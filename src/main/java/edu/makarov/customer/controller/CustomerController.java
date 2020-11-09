package edu.makarov.customer.controller;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.servise.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("{id}")
    public Customer findById(@PathVariable("id") long id) {
        return customerService.findById(id);
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PutMapping("{id}")
    public Customer update(@PathVariable("id") long id, @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        customerService.delete(id);
    }
}
