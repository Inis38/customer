package edu.makarov.customer.servise.impl;

import edu.makarov.customer.models.Card;
import edu.makarov.customer.repository.CardRepository;
import edu.makarov.customer.servise.AccountService;
import edu.makarov.customer.servise.CardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    @Override
    public List<Card> findAllByAccountId(long id) {
        return cardRepository.findCardByAccountId(id);
    }

    @Override
    public Card findById(long id) {
        return cardRepository.findById(id).get();
    }

    @Override
    public Card create(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card create(Card card, long accountId) {
        card.setAccount(accountService.findById(accountId));
        return create(card);
    }

    @Override
    public Card update(long id, Card card) {
        Card cardFromDb = findById(id);
        BeanUtils.copyProperties(card, cardFromDb, "id");
        return create(cardFromDb);
    }

    @Override
    public void delete(long id) {
        cardRepository.deleteById(id);
    }

}
