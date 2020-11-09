package edu.makarov.customer.servise;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Card;

import java.util.List;

public interface CardService extends BaseService<Card> {

    List<Card> findAllByAccountId(long id);

    Card create(Card account, long customerId);
}
