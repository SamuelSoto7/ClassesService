package com.classes.classesService.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Method to notify about schedule changes
    // Sends a message to the "schedule-exchange" fanout exchange
    public void notifyScheduleChange(String message) {
        rabbitTemplate.convertAndSend("schedule-exchange", "", message);
    }
}