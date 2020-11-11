package edu.makarov.customer.controller;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class MainController {


    @GetMapping
    public String mainPage() {
        return "Main page";
    }

}
