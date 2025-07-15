package com.bankly.paymentservice.kafka;


import com.bankly.paymentservice.event.TransactionEvent;
import com.bankly.paymentservice.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    private PaymentService paymentService;

    public TransactionConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "transaction-initiated", groupId = "${spring.kafka.consumer.group-id}")
    public void listenTransactionInitiated(TransactionEvent event) {
        System.out.println("Received TransactionInitiatedEvent: " + event.getTransactionId());
        // Asynchronous payment processing logic here
        // This could involve more complex business logic, calling external systems, etc.
        paymentService.processPaymentAsync(event);
    }
}
