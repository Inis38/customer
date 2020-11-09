package edu.makarov.customer.controller;

import edu.makarov.customer.models.Card;
import edu.makarov.customer.servise.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/accounts/{accountId}/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping
    public List<Card> findAll(@PathVariable("accountId") long accountId) {
        return cardService.findAllByAccountId(accountId);
    }

    @GetMapping("{id}")
    public Card findById(@PathVariable("id") long id) {
        return cardService.findById(id);
    }

    @PostMapping
    public Card create(@RequestBody Card card, @PathVariable("accountId") long accountId) {
        return cardService.create(card, accountId);
    }

    @PutMapping("{id}")
    public Card update(@PathVariable("id") long id, @RequestBody Card card) {
        return cardService.update(id, card);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        cardService.delete(id);
    }
}
