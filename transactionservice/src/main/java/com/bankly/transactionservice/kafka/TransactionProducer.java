package com.bankly.transactionservice.kafka;


import com.bankly.transactionservice.event.TransactionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionProducer {

    private static final String TOPIC = "transaction-initiated";
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public TransactionProducer(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionInitiatedEvent(TransactionEvent event) {
        kafkaTemplate.send(TOPIC, event.getTransactionId().toString(), event);
        System.out.println("Produced TransactionInitiatedEvent for transaction ID: " + event.getTransactionId());
    }
}
