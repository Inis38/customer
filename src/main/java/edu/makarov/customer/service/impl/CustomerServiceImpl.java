package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.models.dto.SubscriptionManagementDTO;
import edu.makarov.customer.repository.CustomerRepository;
import edu.makarov.customer.repository.SubscriptionRepository;
import edu.makarov.customer.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, SubscriptionRepository subscriptionRepository) {
        this.customerRepository = customerRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(long id) {
        logger.info("Запрос информации о клиенте с id {}", id);
        return customerRepository.findById(id);
    }

    @Override
    public Customer create(Customer customer) {
        logger.info("Сохраняем клиента - {}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> update(long id, Customer customer) {
        logger.info("Обновление информации о клиенте с id {}, новые данные - {}", id,  customer);
        return findById(id)
                .map(customerFromDb -> {
                    BeanUtils.copyProperties(customer, customerFromDb, "id");
                    return Optional.of(create(customerFromDb));
                })
                .orElse(Optional.empty());
    }

    @Override
    public boolean delete(long id) {
        if (!findById(id).isPresent()) {
            logger.warn("Не удалось удалить клиента с id {}", id);
            return false;
        }
        logger.warn("Клиент с id {} удален", id);
        customerRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<List<Subscription>> findSubscriptions(long customerId) {
        logger.info("Запрос списка подписок клиента с id {}", customerId);
        return findById(customerId)
                .map(subscriptionRepository::findSubscriptionsByCustomers)
                .orElse(Optional.empty());
    }

    @Override
    public Optional<Set<Subscription>> addSubscription(SubscriptionManagementDTO subDto) {

        Optional<Customer> customer = findById(subDto.getCustomerId());
        Optional<Subscription> subscription = subscriptionRepository.findById(subDto.getSubscriptionId());
        if (!customer.isPresent() || !subscription.isPresent()) {
            logger.warn("Клиента с id {} или подписки с id {} не существует", subDto.getCustomerId(), subDto.getSubscriptionId());
            return Optional.empty();
        }
        Customer customerFromDb = customer.get();
        Set<Subscription> subscriptionsFromDb = customerFromDb.getSubscriptions();
        subscriptionsFromDb.add(subscription.get());
        logger.info("Клиенту с id {} добавлена подписка с id {}", subDto.getCustomerId(), subDto.getSubscriptionId());

        return Optional.of(create(customerFromDb).getSubscriptions());
    }
}
