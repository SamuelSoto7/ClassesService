package com.classes.classesService.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.classes.classesService.model.Payment;

@Service
public class PaymentDLQListener {

    // Listener for "payment-dlq"
    // This method processes failed payment messages received from the "payment-dlq" queue
    @RabbitListener(queues = "payment-dlq")
    public void processFailedPayment(Payment payment) {
        System.out.println("Failed payment received in DLQ: " + payment);
    }
}
