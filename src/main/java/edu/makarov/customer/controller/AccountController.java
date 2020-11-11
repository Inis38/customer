package edu.makarov.customer.controller;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/accounts")
@ApiOperation(value = "/customers/{customerId}/accounts", tags = "Account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "Fetch All Accounts For Customer", response = Iterable.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> findAll(@PathVariable("customerId") long customerId) {
        List<Account> accounts = accountService.findAllByCustomerId(customerId);
        if (accounts == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Account by Id", response = Account.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> findById(@PathVariable("id") long id) {
        Account account = accountService.findById(id);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @ApiOperation(value = "Insert Account Record", response = Account.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> create(@RequestBody Account account, @PathVariable("customerId") long customerId) {
        Account accountFromDb = accountService.create(account, customerId);
        if (accountFromDb == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(accountFromDb, HttpStatus.OK);
    }

    @ApiOperation(value = "Update Account Details", response = Account.class)
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> update(@PathVariable("id") long id, @RequestBody Account account) {
        Account accountFromDb = accountService.update(id, account);
        if (accountFromDb == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(accountFromDb, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an Account")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> delete(@PathVariable("id") long id) {
        if (!accountService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
