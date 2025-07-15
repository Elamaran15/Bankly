package com.bankly.transactionservice.kafka;



import com.bankly.transactionservice.event.TransactionSuccessEvent;
import com.bankly.transactionservice.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessedConsumer {

    private final TransactionService transactionService;

    public TransactionProcessedConsumer(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "transaction-processed", groupId = "${spring.kafka.consumer.group-id}")
    public void listenTransactionInitiated(TransactionSuccessEvent event) {
        transactionService.updateProcessedTransactionStatus(event.getTransactionId(),event.getTransactionStatus());
    }
}
