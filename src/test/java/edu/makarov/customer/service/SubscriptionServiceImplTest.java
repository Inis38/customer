package edu.makarov.customer.service;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
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

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionServiceImplTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    private Subscription subscription;

    @Before
    public void initModels() {
        subscription = new Subscription();
        subscription.setName("Услуга");
    }

    @Test
    public void findAllTest() {
        List<Subscription> subscriptions = subscriptionService.findAll();

        Mockito.verify(subscriptionRepository, Mockito.times(1)).findAll();
        Assert.assertNotNull(subscriptions);
    }

    @Test
    public void findByIdTest() {
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionRepository).findById(3L);

        Subscription subscriptionFromDb = subscriptionService.findById(3);

        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNotNull(subscriptionFromDb);
        Assert.assertEquals(subscription.getName(), subscriptionFromDb.getName());
    }

    @Test
    public void findByIdFailTest() {
        Mockito.doReturn(Optional.empty()).when(subscriptionRepository).findById(3L);

        Subscription subscriptionFromDb = subscriptionService.findById(3);

        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Assert.assertNull(subscriptionFromDb);
    }

    @Test
    public void createSubscriptionTest() {
        Mockito.doReturn(subscription).when(subscriptionRepository).save(subscription);

        Subscription subscriptionFromDb = subscriptionService.create(subscription);

        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertNotNull(subscriptionFromDb);
        Assert.assertEquals(subscription.getName(), subscriptionFromDb.getName());
    }

    @Test
    public void updateSubscriptionTest() {
        subscription.setId(10);
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionRepository).findById(10L);
        Mockito.doReturn(subscription).when(subscriptionRepository).save(subscription);

        Subscription subscriptionFromDb = subscriptionService.update(10, subscription);

        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(Mockito.any());
        Assert.assertEquals(subscription.getId(), subscriptionFromDb.getId());
    }

    @Test
    public void updateSubscriptionFailTest() {
        Mockito.doReturn(Optional.empty()).when(subscriptionRepository).findById(10L);
        Mockito.doReturn(subscription).when(subscriptionRepository).save(subscription);

        Subscription subscriptionFromDb = subscriptionService.update(10, subscription);

        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(subscriptionRepository, Mockito.times(0)).save(Mockito.any());
        Assert.assertNull(subscriptionFromDb);
    }

    @Test
    public void deleteSubscriptionTest() {
        Mockito.doReturn(Optional.of(subscription)).when(subscriptionRepository).findById(10L);

        boolean result = subscriptionService.delete(10);

        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
        Assert.assertTrue(result);
    }

    @Test
    public void deleteSubscriptionFailTest() {
        Mockito.doReturn(Optional.empty()).when(subscriptionRepository).findById(10L);

        boolean result = subscriptionService.delete(10);

        Mockito.verify(subscriptionRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(subscriptionRepository, Mockito.times(0)).deleteById(Mockito.anyLong());
        Assert.assertFalse(result);
    }
}