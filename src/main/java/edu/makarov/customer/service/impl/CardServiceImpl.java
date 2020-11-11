package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Card;
import edu.makarov.customer.repository.CardRepository;
import edu.makarov.customer.service.AccountService;
import edu.makarov.customer.service.CardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountService accountService;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, AccountService accountService) {
        this.cardRepository = cardRepository;
        this.accountService = accountService;
    }

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    @Override
    public List<Card> findAllByAccountId(long id) {
        if (accountService.findById(id) == null) {
            return null;
        }
        return cardRepository.findCardByAccountId(id);
    }

    @Override
    public Card findById(long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card create(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card create(Card card, long accountId) {
        Account account = accountService.findById(accountId);
        if (account == null) {
            return null;
        }
        card.setAccount(account);
        return create(card);
    }

    @Override
    public Card update(long id, Card card) {
        Card cardFromDb = findById(id);
        if (cardFromDb == null) {
            return null;
        }
        BeanUtils.copyProperties(card, cardFromDb, "id");
        return create(cardFromDb);
    }

    @Override
    public boolean delete(long id) {
        if (findById(id) == null) {
            return false;
        }
        cardRepository.deleteById(id);
        return true;
    }

}
