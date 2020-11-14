package edu.makarov.customer.service;

import edu.makarov.customer.models.Subscription;

public interface SubscriptionService extends BaseService<Subscription> {

    Subscription findById(long id);

    Subscription update(long id, Subscription model);
}
