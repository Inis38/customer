package edu.makarov.customer.consumer;

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
}
