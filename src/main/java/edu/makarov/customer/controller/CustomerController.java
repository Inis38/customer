package edu.makarov.customer.controller;

import edu.makarov.customer.exception.BadRequestException;
import edu.makarov.customer.exception.RecordNotFoundException;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@ApiOperation(value = "/customers", tags = "Customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation(value = "Fetch All Customers", response = Iterable.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Customer>> findAll() {
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Customer by Id", response = Customer.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> findById(@PathVariable("id") long id) {

        return customerService.findById(id)
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Customer with id '" + id + "' not found"));
    }

    @ApiOperation(value = "Insert Customer Record", response = Customer.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {

        return new ResponseEntity<>(customerService.create(customer), HttpStatus.OK);
    }

    @ApiOperation(value = "Update Customer Details", response = Customer.class)
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> update(@PathVariable("id") long id, @RequestBody Customer customer) {

        return customerService.update(id, customer)
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("Failed to update information for customer with id '" + id + "'"));
    }

    @ApiOperation(value = "Delete a Customer")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> delete(@PathVariable("id") long id) {
        if (!customerService.delete(id)) {
            throw new BadRequestException("Failed to delete customer with id '" + id + "'");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Find Subscriptions")
    @RequestMapping(value = "{id}/subscriptions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Subscription>> findSubscriptions(@PathVariable("id") long id) {
        return customerService.findSubscriptions(id)
                .map(subscriptions -> new ResponseEntity<>(subscriptions, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("This client has no subscriptions or a client with this id '" + id + "' does not exist"));
    }
}
