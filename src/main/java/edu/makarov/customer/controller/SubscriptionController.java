package edu.makarov.customer.controller;

import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.service.SubscriptionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subscriptions")
@ApiOperation(value = "/subscriptions", tags = "Customer")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @ApiOperation(value = "Fetch All Subscriptions", response = Iterable.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Subscription>> findAll() {
        return new ResponseEntity<>(subscriptionService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Subscription by Id", response = Subscription.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Subscription> findById(@PathVariable("id") long id) {
        Subscription subscription = subscriptionService.findById(id);
        if (subscription == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    @ApiOperation(value = "Insert Subscription Record", response = Subscription.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Subscription> create(@RequestBody Subscription subscription) {
        return new ResponseEntity<>(subscriptionService.create(subscription), HttpStatus.OK);
    }

    @ApiOperation(value = "Update Subscription Details", response = Subscription.class)
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Subscription> update(@PathVariable("id") long id, @RequestBody Subscription subscription) {
        Subscription subscriptionFromDb = subscriptionService.update(id, subscription);
        if (subscriptionFromDb == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(subscriptionFromDb, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Subscription")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Subscription> delete(@PathVariable("id") long id) {
        if (!subscriptionService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
