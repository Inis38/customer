package edu.makarov.customer.controller;

import edu.makarov.customer.exception.BadRequestException;
import edu.makarov.customer.exception.RecordNotFoundException;
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

        return cardService.findAllByAccountId(accountId)
                .map(cards -> new ResponseEntity<>(cards, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Could not find cards for account with id '" + accountId + "'"));
    }

    @ApiOperation(value = "Fetch Card by Id", response = Card.class)
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> findById(@PathVariable("id") long id) {

        return cardService.findById(id)
                .map(card -> new ResponseEntity<>(card, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Could not find card with id '" + id + "'"));
    }

    @ApiOperation(value = "Insert Card Record", response = Card.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> create(@RequestBody Card card, @PathVariable("accountId") long accountId) {

        return cardService.create(card, accountId)
                .map(cardFromDb -> new ResponseEntity<>(cardFromDb, HttpStatus.OK))
                .orElseThrow(() -> new RecordNotFoundException("Failed to create card. Account with id '" + accountId + "' not found"));
    }

    @ApiOperation(value = "Update Card Details", response = Card.class)
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> update(@PathVariable("id") long id, @RequestBody Card card) {

        return cardService.update(id, card)
                .map(cardFromDb -> new ResponseEntity<>(cardFromDb, HttpStatus.OK))
                .orElseThrow(() -> new BadRequestException("Failed to update card information with id '" + id + "'"));
    }

    @ApiOperation(value = "Delete a Card")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Card> delete(@PathVariable("id") long id) {
        if (!cardService.delete(id)) {
            throw new BadRequestException("Failed to delete card with id '" + id + "'. It may not exist.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
