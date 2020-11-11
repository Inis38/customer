package edu.makarov.customer.service;

import edu.makarov.customer.models.Card;

import java.util.List;

public interface CardService extends BaseService<Card> {

    List<Card> findAllByAccountId(long id);

    Card create(Card account, long customerId);
}
