package edu.makarov.customer.controller;

import edu.makarov.customer.exception.ErrorResponse;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
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
public class SubscriptionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void findAllTest() {
        ResponseEntity<List<Subscription>> response = restTemplate.exchange("/subscriptions",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Subscription>>() {});
        List<Subscription> subscriptions = response.getBody();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(subscriptions);
        Assert.assertFalse(subscriptions.isEmpty());
        Assert.assertEquals(subscriptions.size(), 5);
    }

    @Test
    public void findByIdTest() {
        ResponseEntity<Subscription> response = restTemplate.getForEntity("/subscriptions/{id}", Subscription.class, 2);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getName(), "Услуга повышенный кэшбэк");
    }

    @Test
    public void findByIdFailTest() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/subscriptions/{id}", ErrorResponse.class, 20);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void createTest() {
        Subscription subscription = new Subscription();
        subscription.setName("name");
        HttpEntity<Subscription> entity = new HttpEntity<>(subscription);

        ResponseEntity<Subscription> response = restTemplate.exchange("/subscriptions", HttpMethod.POST, entity, Subscription.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getName(), subscription.getName());
    }

    @Test
    public void updateTest() {
        Subscription subscription = new Subscription();
        subscription.setName("name");
        HttpEntity<Subscription> entity = new HttpEntity<>(subscription);

        ResponseEntity<Subscription> response = restTemplate.exchange("/subscriptions/{id}", HttpMethod.PUT, entity, Subscription.class, 5);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getId(), 5);
        Assert.assertEquals(response.getBody().getName(), subscription.getName());
    }

    @Test
    public void deleteTest() {
        ResponseEntity<Subscription> response = restTemplate.exchange("/subscriptions/{id}", HttpMethod.DELETE, null, Subscription.class, 5);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteFailTest() {
        ResponseEntity<Subscription> response = restTemplate.exchange("/customers/{id}", HttpMethod.DELETE, null, Subscription.class, 10);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
