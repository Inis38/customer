package edu.makarov.customer.controller;

import edu.makarov.customer.exception.RecordNotFoundException;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    public MqController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation(value = "Fetch Customer by Id", response = Customer.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> findById(@PathVariable("id") long id) {

        return customerService.sendCustomerToQueue(id)
                .map(customer ->  new ResponseEntity<>(customer, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Customer with id '" + id + "' not found"));
    }
}
