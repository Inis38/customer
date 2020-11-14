package edu.makarov.customer.controller;

import edu.makarov.customer.exception.BadRequestException;
import edu.makarov.customer.exception.RecordNotFoundException;
import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.dto.BalanceChangeDTO;
import edu.makarov.customer.models.dto.MoneyTransactionDTO;
import edu.makarov.customer.service.AccountService;
import io.swagger.annotations.*;
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

        return accountService.findAllByCustomerId(customerId)
                .map(accounts -> new ResponseEntity<>(accounts, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Customer with id '" + customerId + "' not found"));
    }

    @ApiOperation(value = "Fetch Account by Id", response = Account.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> findById(@PathVariable("id") long id) {

        return accountService.findById(id)
                .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Couldn't find account. Account with id '" + id + "' not found"));
    }

    @ApiOperation(value = "Insert Account Record", response = Account.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> create(@RequestBody Account account, @PathVariable("customerId") long customerId) {

        return accountService.create(account, customerId)
                .map(accountFromDb -> new ResponseEntity<>(accountFromDb, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Failed to create account. Customer with id '" + customerId + "' not found"));
    }

    @ApiOperation(value = "Update Account Details", response = Account.class)
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> update(@PathVariable("id") long id, @RequestBody Account account) {

        return accountService.update(id, account)
                .map(accountFromDb -> new ResponseEntity<>(accountFromDb, HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("Failed to update account details with id '" + id + "'"));
    }

    @ApiOperation(value = "Delete an Account")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> delete(@PathVariable("id") long id) {
        if (!accountService.delete(id)) {
            throw new BadRequestException("Failed to delete account with id '" + id + "'. It may not exist.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Top Up Account Balance", response = Account.class)
    @RequestMapping(value = "{id}/addbalance", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> addBalance(@PathVariable("id") long id, @RequestBody BalanceChangeDTO balanceChangeDTO) {

        balanceChangeDTO.setId(id);
        return accountService.increaseBalance(balanceChangeDTO)
                .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("Failed to top up the balance for the account with id '" + id + "'"));
    }

    @ApiOperation(value = "Pay For Purchase", response = Account.class)
    @RequestMapping(value = "{id}/pay", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> pay(@PathVariable("id") long id, @RequestBody BalanceChangeDTO balanceChangeDTO) {

        balanceChangeDTO.setId(id);
        return accountService.increaseBalance(balanceChangeDTO)
                .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("Failed to write off funds from the account with id '" + id + "'"));
    }

    @ApiOperation(value = "Transfer Money To Another Account", response = Account.class)
    @RequestMapping(value = "{id}/transfer", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> transfer(@PathVariable("id") long id, @RequestBody MoneyTransactionDTO moneyTransactionDTO) {
        moneyTransactionDTO.setReduceId(id);
        if (!accountService.transferMoney(moneyTransactionDTO)) {
            throw new BadRequestException("Failed to transfer money to account with id '" + moneyTransactionDTO.getIncreaseId() + "'.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
