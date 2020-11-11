package edu.makarov.customer.controller;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Card;
import edu.makarov.customer.service.CardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/accounts/{accountId}/cards")
@ApiOperation(value = "/customers/{customerId}/accounts/{accountId}/cards", tags = "Card")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping
    @ApiOperation(value = "Fetch All Cards For Account", response = Iterable.class)
    public List<Card> findAll(@PathVariable("accountId") long accountId) {
        return cardService.findAllByAccountId(accountId);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Fetch Card by Id", response = Card.class)
    public Card findById(@PathVariable("id") long id) {
        return cardService.findById(id);
    }

    @PostMapping
    @ApiOperation(value = "Insert Card Record", response = Card.class)
    public Card create(@RequestBody Card card, @PathVariable("accountId") long accountId) {
        return cardService.create(card, accountId);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update Card Details", response = Card.class)
    public Card update(@PathVariable("id") long id, @RequestBody Card card) {
        return cardService.update(id, card);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete a Card")
    public void delete(@PathVariable("id") long id) {
        cardService.delete(id);
    }
}
