package edu.makarov.customer.service;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.models.dto.SubscriptionManagementDTO;
import edu.makarov.customer.repository.CustomerRepository;
import edu.makarov.customer.repository.SubscriptionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    private Customer customer;

    @Before
    public void initModels() {
        customer = new Customer();
        customer.setId(1);
        customer.setFullName("Иванов");
        customer.setDocumentNumber("12345");
    }

    @Test
    public void findAllTest() {
        List<Customer> customers = customerService.findAll();

        Mockito.verify(customerRepository, Mockito.times(1)).findAll();
        Assert.assertNotNull(customers);
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(3L);

        Optional<Customer> customerFromDb = customerService.findById(3);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertTrue(customerFromDb.isPresent());
        Assert.assertEquals(customer.getFullName(), customerFromDb.get().getFullName());
    }

    @Test
    public void findByIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(3L);

        Optional<Customer> customerFromDb = customerService.findById(3);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertFalse(customerFromDb.isPresent());
    }

    @Test
    public void createCustomerTest() {
        Mockito.doReturn(customer).when(customerRepository).save(customer);

        Customer customerFromDb = customerService.create(customer);

        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertNotNull(customerFromDb);
        Assert.assertEquals(customer.getFullName(), customerFromDb.getFullName());
    }

    @Test
    public void updateCustomerTest() {
        customer.setId(10);
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(10L);
        Mockito.doReturn(customer).when(customerRepository).save(customer);

        Optional<Customer> customerFromDb = customerService.update(10, customer);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertTrue(customerFromDb.isPresent());
        Assert.assertEquals(customer.getId(), customerFromDb.get().getId());
    }

    @Test
    public void updateCustomerFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(10L);
        Mockito.doReturn(customer).when(customerRepository).save(customer);

        Optional<Customer> customerFromDb = customerService.update(10, customer);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertFalse(customerFromDb.isPresent());
    }

    @Test
    public void deleteCustomerTest() {
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(10L);

        boolean result = customerService.delete(10);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerFailTest() {
        Mockito.doReturn(Optional.empty()).when(customerRepository).findById(10L);

        boolean result = customerService.delete(10);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
        Assert.assertFalse(result);
    }

    @Test
    public void findSubscriptionsTest() {
        List<Subscription> list = new ArrayList<>();
        list.add(new Subscription());
        Mockito.doReturn(Optional.of(list)).when(subscriptionRepository).findSubscriptionsByCustomers(Mockito.any());
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(Mockito.anyLong());

        Optional<List<Subscription>> subscriptions = customerService.findSubscriptions(1);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).findSubscriptionsByCustomers(Mockito.any());
        Assert.assertTrue(subscriptions.isPresent());
    }

    @Test
    public void findSubscriptionsFailTest() {
        Mockito.doReturn(Optional.empty()).when(subscriptionRepository).findSubscriptionsByCustomers(Mockito.any());
        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(Mockito.anyLong());

        Optional<List<Subscription>> subscriptions = customerService.findSubscriptions(1);

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).findSubscriptionsByCustomers(Mockito.any());
        Assert.assertFalse(subscriptions.isPresent());
    }

    @Test
    public void addSubscriptionTest() {
        Subscription subscription1 = new Subscription();
        subscription1.setId(1);
        Subscription subscription2 = new Subscription();
        subscription2.setId(2);

        Set<Subscription> set = new HashSet<>();
        set.add(subscription1);
        customer.setSubscriptions(set);

        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(Mockito.anyLong());
        Mockito.doReturn(customer).when(customerRepository).save(Mockito.any());
        Mockito.doReturn(Optional.of(subscription2)).when(subscriptionRepository).findById(Mockito.anyLong());

        Optional<Set<Subscription>> subscriptionsFromDb = customerService.addSubscription(new SubscriptionManagementDTO(1, 2));

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Assert.assertTrue(subscriptionsFromDb.isPresent());
        Assert.assertEquals(subscriptionsFromDb.get().size(), 2);
    }

    @Test
    public void addSubscriptionFailTest() {
        Subscription subscription1 = new Subscription();
        subscription1.setId(1);
        Subscription subscription2 = new Subscription();
        subscription2.setId(2);

        Set<Subscription> set = new HashSet<>();
        set.add(subscription1);
        customer.setSubscriptions(set);

        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(Mockito.anyLong());
        Mockito.doReturn(customer).when(customerRepository).save(Mockito.any());
        Mockito.doReturn(Optional.empty()).when(subscriptionRepository).findById(Mockito.anyLong());

        Optional<Set<Subscription>> subscriptionsFromDb = customerService.addSubscription(new SubscriptionManagementDTO(1, 2));

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Assert.assertFalse(subscriptionsFromDb.isPresent());
    }

    @Test
    public void deleteSubscriptionTest() {
        Subscription subscription = new Subscription();
        subscription.setId(1);

        Set<Subscription> set = new HashSet<>();
        set.add(subscription);
        customer.setSubscriptions(set);

        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(Mockito.anyLong());
        Mockito.doReturn(customer).when(customerRepository).save(Mockito.any());
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionRepository).findById(Mockito.anyLong());

        Optional<Set<Subscription>> subscriptionsFromDb = customerService.deleteSubscription(new SubscriptionManagementDTO(1, 1));

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Assert.assertTrue(subscriptionsFromDb.isPresent());
        Assert.assertEquals(subscriptionsFromDb.get().size(), 0);
    }

    @Test
    public void deleteSubscriptionFailTest() {
        Subscription subscription = new Subscription();
        subscription.setId(1);

        Set<Subscription> set = new HashSet<>();
        set.add(subscription);
        customer.setSubscriptions(set);

        Mockito.doReturn(Optional.of(customer)).when(customerRepository).findById(Mockito.anyLong());
        Mockito.doReturn(customer).when(customerRepository).save(Mockito.any());
        Mockito.doReturn(Optional.empty()).when(subscriptionRepository).findById(Mockito.anyLong());

        Optional<Set<Subscription>> subscriptionsFromDb = customerService.deleteSubscription(new SubscriptionManagementDTO(1, 2));

        Mockito.verify(customerRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(customerRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Assert.assertFalse(subscriptionsFromDb.isPresent());
    }
}