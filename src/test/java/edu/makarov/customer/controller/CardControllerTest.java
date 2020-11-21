package edu.makarov.customer.controller;

import edu.makarov.customer.exception.ErrorResponse;
import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Card;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CardControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void findAllTest() {
        ResponseEntity<List<Card>> response = restTemplate.exchange("/customers/1/accounts/1/cards",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>() {});
        List<Card> cards = response.getBody();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(cards);
        Assert.assertFalse(cards.isEmpty());
        Assert.assertEquals(cards.size(), 2);
    }

    @Test
    public void findAllFailTest() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/customers/{id}/accounts/{id}/cards",
                ErrorResponse.class, 1, 10);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void findByIdTest() {
        ResponseEntity<Card> response = restTemplate.getForEntity("/customers/{id}/accounts/{id}/cards/{id}",
                Card.class, 1, 4, 6);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getCardNumber(), "8972 3461 2837 4597");
    }

    @Test
    public void findByIdFailTest() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/customers/{id}/accounts/{id}/cards/{id}",
                ErrorResponse.class, 1, 1, 20);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void createTest() {
        Card card = new Card();
        card.setCardNumber("2222 2222 2222 2222");
        HttpEntity<Card> entity = new HttpEntity<>(card);

        ResponseEntity<Card> response = restTemplate.exchange("/customers/{id}/accounts/{id}/cards",
                HttpMethod.POST, entity, Card.class, 1, 1);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getCardNumber(), card.getCardNumber());
    }

    @Test
    public void createFailTest() {
        Card card = new Card();
        card.setCardNumber("2222 2222 2222 2222");
        HttpEntity<Card> entity = new HttpEntity<>(card);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/customers/{id}/accounts/{id}/cards",
                HttpMethod.POST, entity, ErrorResponse.class, 1, 10);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateTest() {
        Card card = new Card();
        card.setCardNumber("2222 2222 2222 2222");
        HttpEntity<Card> entity = new HttpEntity<>(card);

        ResponseEntity<Card> response = restTemplate.exchange("/customers/{id}/accounts/{id}/cards/{id}",
                HttpMethod.PUT, entity, Card.class, 1, 1, 1);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getId(), 1);
        Assert.assertEquals(response.getBody().getCardNumber(), card.getCardNumber());
    }

    @Test
    public void updateFailTest() {
        Card card = new Card();
        card.setCardNumber("2222 2222 2222 2222");
        HttpEntity<Card> entity = new HttpEntity<>(card);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/customers/{id}/accounts/{id}/cards/{id}",
                HttpMethod.PUT, entity, ErrorResponse.class, 1, 1, 20);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteTest() {
        ResponseEntity<Card> response = restTemplate.exchange("/customers/{id}/accounts/{id}/cards/{id}",
                HttpMethod.DELETE, null, Card.class, 1, 1, 2);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteFailTest() {
        ResponseEntity<Card> response = restTemplate.exchange("/customers/{id}/accounts/{id}/cards/{id}",
                HttpMethod.DELETE, null, Card.class, 1, 1, 10);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}