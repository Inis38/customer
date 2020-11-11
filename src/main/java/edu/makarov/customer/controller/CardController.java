package edu.makarov.customer.controller;

import edu.makarov.customer.models.Card;
import edu.makarov.customer.service.CardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/accounts/{accountId}/cards")
@ApiOperation(value = "/customers/{customerId}/accounts/{accountId}/cards", tags = "Card")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @ApiOperation(value = "Fetch All Cards For Account", response = Iterable.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Card>> findAll(@PathVariable("accountId") long accountId) {
        List<Card> cards = cardService.findAllByAccountId(accountId);
        if (cards == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @ApiOperation(value = "Fetch Card by Id", response = Card.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> findById(@PathVariable("id") long id) {
        Card card = cardService.findById(id);
        if (card == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @ApiOperation(value = "Insert Card Record", response = Card.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> create(@RequestBody Card card, @PathVariable("accountId") long accountId) {
        Card cardFromDb = cardService.create(card, accountId);
        if (cardFromDb == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cardFromDb, HttpStatus.OK);
    }

    @ApiOperation(value = "Update Card Details", response = Card.class)
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> update(@PathVariable("id") long id, @RequestBody Card card) {
        Card cardFromDb = cardService.update(id, card);
        if (cardFromDb == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cardFromDb, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a Card")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> delete(@PathVariable("id") long id) {
        if (!cardService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
