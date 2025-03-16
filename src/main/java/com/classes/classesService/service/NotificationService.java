package com.classes.classesService.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Method to send a registration notification
    // Sends a message to the "registration-exchange" with the routing key "registration-routing-key"
    public void sendRegistrationNotification(String message) {
        rabbitTemplate.convertAndSend("registration-exchange", "registration-routing-key", message);
    }
}