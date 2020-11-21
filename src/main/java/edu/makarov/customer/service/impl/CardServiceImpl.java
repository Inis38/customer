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

/**
 * Class for performing operations with bank cards
 */
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

    /**
     * Find all bank cards in the database
     *
     * @return List of Card
     */
    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    /**
     * Find all cards linked to the specified account
     *
     * @param id Account id
     * @return List of Card
     */
    @Override
    public Optional<List<Card>> findAllByAccountId(long id) {
        logger.info("Запрос всех карт для счета с id {}", id);
        return accountService.findById(id)
                .map(cardRepository::findCardsByAccount)
                .orElse(Optional.empty());
    }

    /**
     * Search for a Card by its id
     *
     * @param id Card id
     * @return Card
     */
    @Override
    public Optional<Card> findById(long id) {
        logger.info("Запрос информации о карте с id {}", id);
        return cardRepository.findById(id);
    }

    /**
     * Save Card to database
     *
     * @param card Card to be kept
     * @return Saved Card
     */
    @Override
    public Card create(Card card) {
        logger.info("Сохраняем карту для аккаунта с id {}, {}", card.getAccount().getId(), card);
        return cardRepository.save(card);
    }

    /**
     * Save the Card for a specific Account
     *
     * @param card Card to be kept
     * @param accountId Id of the account to which the card will be linked
     * @return Saved Card
     */
    @Override
    public Optional<Card> create(Card card, long accountId) {

        return accountService.findById(accountId)
                .map(account -> {
                    card.setAccount(account);
                    return Optional.of(create(card));
                })
                .orElse(Optional.empty());
    }

    /**
     * Refresh Card information
     *
     * @param id Card id
     * @param card New Card information
     * @return Saved Card
     */
    @Override
    public Optional<Card> update(long id, Card card) {

        return findById(id)
                .map(cardFromDb -> {
                    logger.info("Обновляем информацию о карте для аккаунта с id {}, {}", cardFromDb.getAccount().getId(), card);
                    BeanUtils.copyProperties(card, cardFromDb, "id", "account");
                    return Optional.of(create(cardFromDb));
                })
                .orElse(Optional.empty());
    }

    /**
     * Delete card
     *
     * @param id Card id
     * @return True, if a card with such id does not exist, then false
     */
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
