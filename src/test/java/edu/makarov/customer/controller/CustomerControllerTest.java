package edu.makarov.customer.controller;

import edu.makarov.customer.exception.ErrorResponse;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.models.dto.SubscriptionManagementDTO;
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
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void findAllTest() {
        ResponseEntity<List<Customer>> response = restTemplate.exchange("/customers",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>>() {});
        List<Customer> customers = response.getBody();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(customers);
        Assert.assertFalse(customers.isEmpty());
        Assert.assertEquals(customers.size(), 5);
    }

    @Test
    public void findByIdTest() {
        ResponseEntity<Customer> response = restTemplate.getForEntity("/customers/{id}", Customer.class, 2);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getDocumentNumber(), "9922 762811");
    }

    @Test
    public void findByIdFailTest() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/customers/{id}", ErrorResponse.class, 20);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void createTest() {
        Customer customer = new Customer();
        customer.setFullName("full name");
        HttpEntity<Customer> entity = new HttpEntity<>(customer);

        ResponseEntity<Customer> response = restTemplate.exchange("/customers", HttpMethod.POST, entity, Customer.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getFullName(), customer.getFullName());
    }

    @Test
    public void updateTest() {
        Customer customer = new Customer();
        customer.setFullName("full name");
        HttpEntity<Customer> entity = new HttpEntity<>(customer);

        ResponseEntity<Customer> response = restTemplate.exchange("/customers/{id}", HttpMethod.PUT, entity, Customer.class, 1);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getId(), 1);
        Assert.assertEquals(response.getBody().getFullName(), customer.getFullName());
    }

    @Test
    public void deleteTest() {
        ResponseEntity<Customer> response = restTemplate.exchange("/customers/{id}", HttpMethod.DELETE, null, Customer.class, 5);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteFailTest() {
        ResponseEntity<Customer> response = restTemplate.exchange("/customers/{id}", HttpMethod.DELETE, null, Customer.class, 10);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void findSubscriptionsTest() {
        ResponseEntity<List<Subscription>> response = restTemplate.exchange("/customers/{id}/subscriptions",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Subscription>>() {}, 2);
        List<Subscription> subscriptions = response.getBody();
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(subscriptions);
        Assert.assertFalse(subscriptions.isEmpty());
        Assert.assertEquals(subscriptions.size(), 2);
    }

    @Test
    public void addSubscriptionTest() {
        SubscriptionManagementDTO subscriptionManagement = new SubscriptionManagementDTO(2, 3);
        HttpEntity<SubscriptionManagementDTO> entity = new HttpEntity<>(subscriptionManagement);

        ResponseEntity<Set<Subscription>> response = restTemplate.exchange("/customers/{id}/subscriptions",
                HttpMethod.PUT, entity, new ParameterizedTypeReference<Set<Subscription>>() {}, 2);
        Set<Subscription> subscriptions = response.getBody();

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(subscriptions);
        Assert.assertFalse(subscriptions.isEmpty());
        Assert.assertEquals(subscriptions.size(), 2);
    }

    @Test
    public void delSubscriptionTest() {
        SubscriptionManagementDTO subscriptionManagement = new SubscriptionManagementDTO(4, 4);
        HttpEntity<SubscriptionManagementDTO> entity = new HttpEntity<>(subscriptionManagement);

        ResponseEntity<Set<Subscription>> response = restTemplate.exchange("/customers/{id}/subscriptions",
                HttpMethod.DELETE, entity, new ParameterizedTypeReference<Set<Subscription>>() {}, 4);
        Set<Subscription> subscriptions = response.getBody();

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(subscriptions);
        Assert.assertFalse(subscriptions.isEmpty());
        Assert.assertEquals(subscriptions.size(), 1);
    }
}