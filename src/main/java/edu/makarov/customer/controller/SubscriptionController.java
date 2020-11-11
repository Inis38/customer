package edu.makarov.customer.controller;

import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.service.SubscriptionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subscriptions")
@ApiOperation(value = "/subscriptions", tags = "Customer")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    @ApiOperation(value = "Fetch All Subscriptions", response = Iterable.class)
    public List<Subscription> findAll() {
        return subscriptionService.findAll();
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Fetch Subscription by Id", response = Subscription.class)
    public Subscription findById(@PathVariable("id") long id) {
        return subscriptionService.findById(id);
    }

    @PostMapping
    @ApiOperation(value = "Insert Subscription Record", response = Subscription.class)
    public Subscription create(@RequestBody Subscription subscription) {
        return subscriptionService.create(subscription);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update Subscription Details", response = Subscription.class)
    public Subscription update(@PathVariable("id") long id, @RequestBody Subscription subscription) {
        return subscriptionService.update(id, subscription);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete a Subscription")
    public void delete(@PathVariable("id") long id) {
        subscriptionService.delete(id);
    }
}
