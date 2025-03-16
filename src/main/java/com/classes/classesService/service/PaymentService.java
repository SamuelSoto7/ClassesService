package com.classes.classesService.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classes.classesService.model.Payment;

@Service
public class PaymentService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendPayment(Payment payment) {
        try {
           
            if (!isPaymentSuccessful(payment)) {
               
                sendToDLQ(payment);
                return;
            }
            
            rabbitTemplate.convertAndSend("payment-exchange", "payment-routing-key", payment);
        } catch (Exception e) {
            
            System.err.println("Error processing payment: " + e.getMessage());
            sendToDLQ(payment); 
        }
    }

    private boolean isPaymentSuccessful(Payment payment) {
        return true;
    }

    private void sendToDLQ(Payment payment) {
        try {
            rabbitTemplate.convertAndSend("", "payment-dlq", payment);
            System.out.println("Payment failed, sent to DLQ: " + payment);
        } catch (Exception e) {
            System.err.println("Error sending to DLQ: " + e.getMessage());
        }
    }
}