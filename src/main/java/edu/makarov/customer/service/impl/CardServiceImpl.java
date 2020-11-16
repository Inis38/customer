package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Card;
import edu.makarov.customer.repository.CardRepository;
import edu.makarov.customer.service.AccountService;
import edu.makarov.customer.service.CardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    private static final Logger logger = LogManager.getLogger(CardServiceImpl.class);
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
    public Optional<List<Card>> findAllByAccountId(long id) {
        logger.info("Запрос всех карт для счета с id {}", id);
        return accountService.findById(id)
                .map(cardRepository::findCardsByAccount)
                .orElse(Optional.empty());
    }

    @Override
    public Optional<Card> findById(long id) {
        logger.info("Запрос информации о карте с id {}", id);
        return cardRepository.findById(id);
    }

    @Override
    public Card create(Card card) {
        logger.info("Сохраняем карту для аккаунта с id {}, {}", card.getAccount().getId(), card);
        return cardRepository.save(card);
    }

    @Override
    public Optional<Card> create(Card card, long accountId) {

        return accountService.findById(accountId)
                .map(account -> {
                    card.setAccount(account);
                    return Optional.of(create(card));
                })
                .orElse(Optional.empty());
    }

    @Override
    public Optional<Card> update(long id, Card card) {
        logger.info("Обновляем информацию о карте для аккаунта с id {}, {}", card.getAccount().getId(), card);
        return findById(id)
                .map(cardFromDb -> {
                    BeanUtils.copyProperties(card, cardFromDb, "id");
                    return Optional.of(create(cardFromDb));
                })
                .orElse(Optional.empty());
    }

    @Override
    public boolean delete(long id) {
        if (!findById(id).isPresent()) {
            logger.warn("Не удалось удлить карту с id {}", id);
            return false;
        }
        logger.info("Карта с id {} удалена", id);
        cardRepository.deleteById(id);
        return true;
    }

}
