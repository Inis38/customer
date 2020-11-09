package edu.makarov.customer.controller;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.servise.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public List<Subscription> findAll() {
        return subscriptionService.findAll();
    }

    @GetMapping("{id}")
    public Subscription findById(@PathVariable("id") long id) {
        return subscriptionService.findById(id);
    }

    @PostMapping
    public Subscription create(@RequestBody Subscription subscription) {
        return subscriptionService.create(subscription);
    }

    @PutMapping("{id}")
    public Subscription update(@PathVariable("id") long id, @RequestBody Subscription subscription) {
        return subscriptionService.update(id, subscription);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        subscriptionService.delete(id);
    }
}
