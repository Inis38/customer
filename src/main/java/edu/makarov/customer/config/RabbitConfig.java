package edu.makarov.customer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${customer.queue}")
    private String queue;

    @Value("${customer.exchange}")
    private String exchange;

    @Value("${customer.routing.key}")
    private String routingKey;

    @Value("${account.queue1}")
    private String accountQueue1;

    @Value("${account.queue2}")
    private String accountQueue2;

    @Value("${account.exchange}")
    private String accountExchange;

    @Value("${account.routing.key}")
    private String accountRoutingKey;

    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    @Bean
    public Queue queueAccount1() {
        return new Queue(accountQueue1);
    }

    @Bean
    public Queue queueAccount2() {
        return new Queue(accountQueue2);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange(accountExchange);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }

    @Bean
    public Binding accountBinding1() {
        return BindingBuilder.bind(queueAccount1()).to(accountExchange()).with(accountRoutingKey);
    }

    @Bean
    public Binding accountBinding2() {
        return BindingBuilder.bind(queueAccount2()).to(accountExchange()).with(accountRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
