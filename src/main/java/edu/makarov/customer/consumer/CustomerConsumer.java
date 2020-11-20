package edu.makarov.customer.consumer;

import edu.makarov.customer.models.Account;
import edu.makarov.customer.models.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerConsumer {

    private static final Logger logger = LogManager.getLogger(CustomerConsumer.class);

    @RabbitListener(queues = "customer_queue")
    public void consumeMessageFromQueue(Customer customer) {
        logger.info("Message received from queue: " + customer);
    }

    @RabbitListener(queues = "account_queue1")
    public void consumeAccountMessageFromQueue1(Account account) {
        logger.info("Message received from queue 'account_queue1' - '{}': ", account);
    }

    @RabbitListener(queues = "account_queue2")
    public void consumeAccountMessageFromQueue2(Account account) {
        logger.info("Message received from queue 'account_queue2' - '{}': ", account);
    }
}
