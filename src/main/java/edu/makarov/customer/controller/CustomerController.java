package edu.makarov.customer.controller;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@ApiOperation(value = "/customers", tags = "Customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @ApiOperation(value = "Fetch All Customers", response = Iterable.class)
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Fetch Customer by Id", response = Customer.class)
    public Customer findById(@PathVariable("id") long id) {
        return customerService.findById(id);
    }

    @PostMapping
    @ApiOperation(value = "Insert Customer Record", response = Customer.class)
    public Customer create(@RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update Customer Details", response = Customer.class)
    public Customer update(@PathVariable("id") long id, @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete a Customer")
    public void delete(@PathVariable("id") long id) {
        customerService.delete(id);
    }
}
