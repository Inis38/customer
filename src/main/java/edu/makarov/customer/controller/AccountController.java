package edu.makarov.customer.controller;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/accounts")
@ApiOperation(value = "/customers/{customerId}/accounts", tags = "Account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    @ApiOperation(value = "Fetch All Accounts For Customer", response = Iterable.class)
    public List<Account> findAll(@PathVariable("customerId") long customerId) {
        return accountService.findAllByCustomerId(customerId);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Fetch Account by Id", response = Account.class)
    public Account findById(@PathVariable("id") long id) {
        return accountService.findById(id);
    }

    @PostMapping
    @ApiOperation(value = "Insert Account Record", response = Account.class)
    public Account create(@RequestBody Account account, @PathVariable("customerId") long customerId) {
        return accountService.create(account, customerId);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update Account Details", response = Account.class)
    public Account update(@PathVariable("id") long id, @RequestBody Account account) {
        return accountService.update(id, account);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete an Account")
    public void delete(@PathVariable("id") long id) {
        accountService.delete(id);
    }
}
