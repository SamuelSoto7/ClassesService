package com.classes.classesService.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Main queue for payments
    @Bean
    public Queue paymentQueue() {
        return QueueBuilder.durable("payment-queue")
                .withArgument("x-dead-letter-exchange", "") // Default exchange for DLQ
                .withArgument("x-dead-letter-routing-key", "payment-dlq") // Route to DLQ
                .withArgument("x-message-ttl", 30000) // Message time-to-live (30 seconds)
                .build();
    }
    // Dead Letter Queue (DLQ) for failed payment messages
    // This queue will store messages that could not be processed successfully in the main payment queue
    @Bean
    public Queue paymentDLQ() {
        return QueueBuilder.durable("payment-dlq").build(); // Creates a durable queue named "payment-dlq"
    }

    // Direct Exchange for payment notifications
    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange("payment-exchange"); // Creates a direct exchange named "payment-exchange"
    }

    // Binding to link the payment queue to the payment exchange
    @Bean
    public Binding bindingPaymentQueue(Queue paymentQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueue)
                .to(paymentExchange)
                .with("payment-routing-key"); // Binds the queue to the exchange with the specified routing key
    }

    // Fanout Exchange for schedule changes
    // A fanout exchange broadcasts messages to all bound queues, making it ideal for distributing schedule updates
    @Bean
    public FanoutExchange scheduleExchange() {
        return new FanoutExchange("schedule-exchange"); // Creates a fanout exchange named "schedule-exchange"
    }

    // Queue 1 for handling schedule changes
    // This queue will receive messages related to schedule updates
    @Bean
    public Queue scheduleQueue1() {
        return QueueBuilder.durable("schedule-queue-1").build(); // Creates a durable queue named "schedule-queue-1"
    }

    // Queue 2 for handling schedule changes
    // This queue is an additional consumer for schedule updates, allowing for parallel processing
    @Bean
    public Queue scheduleQueue2() {
        return QueueBuilder.durable("schedule-queue-2").build(); // Creates a durable queue named "schedule-queue-2"
    }

    // Queue for handling enrollment notifications
    // This queue will store messages related to enrollment events
    @Bean
    public Queue registrationQueue() {
        return QueueBuilder.durable("registration-queue").build(); // Creates a durable queue named "registration-queue"
    }

    // Direct Exchange for enrollment notifications
    // A direct exchange routes messages to queues based on an exact match of the routing key
    @Bean
    public DirectExchange registrationExchange() {
        return new DirectExchange("registration-exchange"); // Creates a direct exchange named "registration-exchange"
    }

    // Binding to link the enrollment queue to the enrollment exchange
    // This ensures that messages sent to the "registration-exchange" with the routing key "registration-routing-key" are routed to the "registration-queue"
    @Bean
    public Binding bindingRegistrationQueue(Queue registrationQueue, DirectExchange registrationExchange) {
        return BindingBuilder.bind(registrationQueue)
                .to(registrationExchange)
                .with("registration-routing-key"); // Binds the queue to the exchange with the specified routing key
    }

    // Binding to link Queue 1 to the schedule exchange
    // This ensures that all messages sent to the "schedule-exchange" are routed to "schedule-queue-1"
    @Bean
    public Binding bindingScheduleQueue1(Queue scheduleQueue1, FanoutExchange scheduleExchange) {
        return BindingBuilder.bind(scheduleQueue1).to(scheduleExchange); // Binds the queue to the fanout exchange
    }

    // Binding to link Queue 2 to the schedule exchange
    // This ensures that all messages sent to the "schedule-exchange" are also routed to "schedule-queue-2"
    @Bean
    public Binding bindingScheduleQueue2(Queue scheduleQueue2, FanoutExchange scheduleExchange) {
        return BindingBuilder.bind(scheduleQueue2).to(scheduleExchange); // Binds the queue to the fanout exchange
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
