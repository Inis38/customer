package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.repository.SubscriptionRepository;
import edu.makarov.customer.service.SubscriptionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class for performing operations with subscriptions
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger logger = LogManager.getLogger(SubscriptionServiceImpl.class);
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Find all Subscriptions in the database
     *
     * @return List of Subscriptions
     */
    @Override
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    /**
     * Find a Subscription by his id
     *
     * @param id Subscription id
     * @return Subscription
     */
    @Override
    public Optional<Subscription> findById(long id) {
        logger.info("Запрос информации о услуге с id {}", id);
        return subscriptionRepository.findById(id);
    }

    /**
     * Save Subscription information
     *
     * @param subscription Subscription information
     * @return Saved Subscription
     */
    @Override
    public Subscription create(Subscription subscription) {
        logger.info("Сохраняем услугу {}", subscription);
        return subscriptionRepository.save(subscription);
    }

    /**
     * Update Subscription information
     *
     * @param id Subscription id
     * @param subscription Subscription information
     * @return Saved Subscription
     */
    @Override
    public Optional<Subscription> update(long id, Subscription subscription) {
        logger.info("Обновляем информацию об услуге {}", subscription);
        return findById(id)
                .map(subscriptionFromDb -> {
                    BeanUtils.copyProperties(subscription, subscriptionFromDb, "id");
                    return Optional.of(create(subscriptionFromDb));
                })
                .orElse(Optional.empty());
    }

    /**
     * Delete Subscription
     *
     * @param id Subscription id
     * @return True, if there is no Subscription with this id, then false
     */
    @Override
    public boolean delete(long id) {
        if (!findById(id).isPresent()) {
            logger.warn("Не удалось удалить услгу с id {}", id);
            return false;
        }
        subscriptionRepository.deleteById(id);
        logger.info("Услга с id {} удалена", id);
        return true;
    }
}
