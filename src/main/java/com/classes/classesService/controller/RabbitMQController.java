package com.classes.classesService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.classes.classesService.model.Payment;
import com.classes.classesService.service.ScheduleService;
import com.classes.classesService.service.NotificationService;
import com.classes.classesService.service.PaymentService;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PaymentService paymentService;

    // Endpoint to send a registration notification
    @PostMapping("/send-notification")
    public String sendNotification(@RequestParam String message) {
        notificationService.sendRegistrationNotification(message);
        return "Notification sent: " + message;
    }

    // Endpoint to notify a schedule change
    @PostMapping("/notify-schedule-change")
    public String notifyScheduleChange(@RequestParam String message) {
        scheduleService.notifyScheduleChange(message);
        return "Schedule change notified: " + message;
    }

    // Endpoint to simulate a payment
    @PostMapping("/simulate-payment")
    public String simulatePayment(@RequestParam double amount) {
        Payment payment = new Payment(amount);
        paymentService.sendPayment(payment);
        return "Payment simulated: " + payment;
    }
}
