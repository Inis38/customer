package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.repository.SubscriptionRepository;
import edu.makarov.customer.service.SubscriptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription findById(long id) {
        return subscriptionRepository.findById(id).orElse(null);
    }

    @Override
    public Subscription create(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription update(long id, Subscription subscription) {
        Subscription subscriptionFromDb = findById(id);
        if (subscriptionFromDb == null) {
            return null;
        }
        BeanUtils.copyProperties(subscription, subscriptionFromDb, "id");
        return create(subscriptionFromDb);
    }

    @Override
    public boolean delete(long id) {
        if (findById(id) == null) {
            return false;
        }
        subscriptionRepository.deleteById(id);
        return true;
    }
}
