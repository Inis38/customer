package edu.makarov.customer.controller;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class MainController {

    private final AccountRepository accountRepository;

    @Autowired
    public MainController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public String getAll() {
        return "Main page";
    }

}
