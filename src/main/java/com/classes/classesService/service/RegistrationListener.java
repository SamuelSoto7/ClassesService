package com.classes.classesService.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RegistrationListener {

    // Listener for "registration-queue"
    // This method processes messages received from the "registration-queue" queue
    @RabbitListener(queues = "registration-queue")
    public void receiveRegistrationNotification(String message) {
        System.out.println("New registration received: " + message);
    }
}
