package edu.makarov.customer.controller;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.servise.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers/{customerId}/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> findAll(@PathVariable("customerId") long customerId) {
        return accountService.findAllByCustomerId(customerId);
    }

    @GetMapping("{id}")
    public Account findById(@PathVariable("id") long id) {
        return accountService.findById(id);
    }

    @PostMapping
    public Account create(@RequestBody Account account, @PathVariable("customerId") long customerId) {
        return accountService.create(account, customerId);
    }

    @PutMapping("{id}")
    public Account update(@PathVariable("id") long id, @RequestBody Account account) {
        return accountService.update(id, account);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        accountService.delete(id);
    }
}
