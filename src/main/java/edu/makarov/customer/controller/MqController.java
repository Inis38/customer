package edu.makarov.customer.controller;

import edu.makarov.customer.exception.RecordNotFoundException;
import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.service.AccountService;
import edu.makarov.customer.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq")
@ApiOperation(value = "/mq", tags = "MQ")
public class MqController {

    private final CustomerService customerService;
    private final AccountService accountService;

    @Autowired
    public MqController(CustomerService customerService, AccountService accountService) {
        this.customerService = customerService;
        this.accountService = accountService;
    }

    @ApiOperation(value = "Fetch Customer by Id", response = Customer.class)
    @RequestMapping(value = "customer/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> findCustomerById(@PathVariable("id") long id) {

        return customerService.sendCustomerToQueue(id)
                .map(customer ->  new ResponseEntity<>(customer, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Customer with id '" + id + "' not found"));
    }

    @ApiOperation(value = "Fetch Account by Id", response = Account.class)
    @RequestMapping(value = "account/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> findAccountById(@PathVariable("id") long id) {

        return accountService.sendAccountToQueue(id)
                .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Couldn't find account. Account with id '" + id + "' not found"));
    }
}
