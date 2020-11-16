package edu.makarov.customer.service.impl;

import edu.makarov.customer.controller.CustomerController;
import edu.makarov.customer.models.Customer;
import edu.makarov.customer.repository.CustomerRepository;
import edu.makarov.customer.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
        logger.info("Новый клиент - {}", customer);
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
}
