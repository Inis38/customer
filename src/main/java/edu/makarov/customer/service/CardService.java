package edu.makarov.customer.service;

import edu.makarov.customer.models.Card;

import java.util.List;
import java.util.Optional;

public interface CardService extends BaseService<Card> {

    Optional<List<Card>> findAllByAccountId(long id);

    Optional<Card> create(Card account, long customerId);
}
