package com.bankly.paymentservice.kafka;


import com.bankly.paymentservice.event.TransactionSuccessEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessedProducer {

    private static final String TOPIC = "transaction-processed";
    private final KafkaTemplate<String, TransactionSuccessEvent> kafkaTemplate;

    public TransactionProcessedProducer(KafkaTemplate<String, TransactionSuccessEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionProcessedEvent(TransactionSuccessEvent event) {
        kafkaTemplate.send(TOPIC, event.getTransactionId().toString(), event);
        System.out.println("Produced transaction-processed for transaction ID: " + event.getTransactionId());
    }
}
