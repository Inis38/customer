package edu.makarov.customer.service.impl;

import edu.makarov.customer.models.Customer;
import edu.makarov.customer.models.Subscription;
import edu.makarov.customer.models.dto.SubscriptionManagementDTO;
import edu.makarov.customer.repository.CustomerRepository;
import edu.makarov.customer.repository.SubscriptionRepository;
import edu.makarov.customer.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

    @Value("${customer.exchange}")
    private String exchange;

    @Value("${customer.routing.key}")
    private String routingKey;

    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RabbitTemplate template;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
                               SubscriptionRepository subscriptionRepository,
                               RabbitTemplate template) {
        this.customerRepository = customerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.template = template;
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
        logger.info("Клиенту с id {} добавляем подписку с id {}", subDto.getCustomerId(), subDto.getSubscriptionId());
        return manageSubscriptions(subDto, ((subscription, subscriptions) -> subscriptions.add(subscription)));
    }

    @Override
    public Optional<Set<Subscription>> deleteSubscription(SubscriptionManagementDTO subDto) {
        logger.info("Удалем подписку с id {} у клиента с id {}", subDto.getSubscriptionId(), subDto.getCustomerId());
        return manageSubscriptions(subDto, ((subscription, subscriptions) -> subscriptions.remove(subscription)));
    }

    private Optional<Set<Subscription>> manageSubscriptions(SubscriptionManagementDTO subDto, BiConsumer<Subscription, Set<Subscription>> manager) {
        Optional<Customer> customer = findById(subDto.getCustomerId());
        Optional<Subscription> subscription = subscriptionRepository.findById(subDto.getSubscriptionId());
        if (!customer.isPresent() || !subscription.isPresent()) {
            logger.warn("Клиента с id {} или подписки с id {} не существует", subDto.getCustomerId(), subDto.getSubscriptionId());
            return Optional.empty();
        }
        Customer customerFromDb = customer.get();
        Set<Subscription> subscriptionsFromDb = customerFromDb.getSubscriptions();
        manager.accept(subscription.get(), subscriptionsFromDb);

        return Optional.of(create(customerFromDb).getSubscriptions());
    }

    @Override
    public Optional<Customer> sendCustomerToQueue(long customerId) {

        return findById(customerId)
                .map(customer -> {
                    template.convertAndSend(exchange, routingKey, customer);
                    return Optional.of(customer);
                })
                .orElse(Optional.empty());
    }
}
