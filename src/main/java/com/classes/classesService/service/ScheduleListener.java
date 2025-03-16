package com.classes.classesService.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ScheduleListener {

    // Listener for "schedule-queue-1"
    // This method processes messages received from the "schedule-queue-1" queue
    @RabbitListener(queues = "schedule-queue-1")
    public void receiveScheduleChange1(String message) {
        System.out.println("Listener 1: Schedule change - " + message);
    }

    // Listener for "schedule-queue-2"
    // This method processes messages received from the "schedule-queue-2" queue
    @RabbitListener(queues = "schedule-queue-2")
    public void receiveScheduleChange2(String message) {
        System.out.println("Listener 2: Schedule change - " + message);
    }
}
