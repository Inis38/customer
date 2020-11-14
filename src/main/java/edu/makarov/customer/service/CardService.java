package edu.makarov.customer.service;

import edu.makarov.customer.models.Card;

import java.util.List;

public interface CardService extends BaseService<Card> {

    Card findById(long id);

    Card update(long id, Card model);

    List<Card> findAllByAccountId(long id);

    Card create(Card account, long customerId);
}
