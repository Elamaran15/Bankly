package com.bankly.transactionservice.service;


import com.bankly.transactionservice.dto.TransactionRequest;
import com.bankly.transactionservice.dto.Users;
import com.bankly.transactionservice.entity.Transaction;
import com.bankly.transactionservice.entity.TransactionStatus;
import com.bankly.transactionservice.event.TransactionEvent;
import com.bankly.transactionservice.exception.ApiClientException;
import com.bankly.transactionservice.exception.TransactionValidationException;
import com.bankly.transactionservice.external.ExternalApiClient;
import com.bankly.transactionservice.kafka.TransactionProcessedConsumer;
import com.bankly.transactionservice.kafka.TransactionProducer;
import com.bankly.transactionservice.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;


import javax.crypto.spec.OAEPParameterSpec;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProducer transactionProducer;
    private final ExternalApiClient apiClient;


    private static final String PAYMENT_SERVICE_VALIDATION_URL = "http://payment-service/api/payments/validate";

    private static final String USER_SERVICE_GET_USER_BY_NAME_URL = "http://user-service/api/users/username/{username}";

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionProducer transactionProducer,
                              ExternalApiClient apiClient) {
        this.transactionRepository = transactionRepository;
        this.transactionProducer = transactionProducer;
        this.apiClient = apiClient;

    }

    @Transactional
    public Transaction initiateTransaction(TransactionRequest request, String userId) {
            Transaction transaction = createPendingTransaction(request, userId);
            transaction = transactionRepository.save(transaction);

            try {
                boolean isValidated = validateWithPaymentService(transaction);
                //boolean isValidated = true;

                if (isValidated) {
                    updateTransactionStatus(transaction, TransactionStatus.VALIDATED);
                    publishTransactionEvent(transaction);
                } else {
                    updateTransactionStatus(transaction, TransactionStatus.REJECTED);
                    throw new TransactionValidationException("Transaction rejected by payment service");
                }
            } catch (ApiClientException e) {
                handleApiError(transaction, e);
                throw new TransactionValidationException("Payment service validation failed: " + e.getMessage());
            }


        return transaction;
    }


    @CircuitBreaker(name = "userService", fallbackMethod = "userServiceValidationFallback")
    @Retry(name = "userService")
    public boolean validateUser(String userName) {
        Map<String, String> uriVariables = Map.of("username", userName);
        Users users= apiClient.getBlocking(USER_SERVICE_GET_USER_BY_NAME_URL,uriVariables, Users.class);
        return users != null;
    }

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentServiceValidationFallback")
    @Retry(name = "paymentService")
    protected boolean validateWithPaymentService(Transaction transaction) {
        Map<String, Object> request = Map.of(
                "transactionId", transaction.getId().toString(),
                "debitAccountNumber", transaction.getDebitAccountNumber(),
                "creditAccountNumber", transaction.getCreditAccountNumber(),
                "amount", transaction.getAmount(),
                "debitCurrency", transaction.getDebitCurrency(),
                "creditCurrency",transaction.getCreditCurrency(),
                "userName",transaction.getUserId()
        );

        return apiClient.postBlocking(
                PAYMENT_SERVICE_VALIDATION_URL,
                request,
                Boolean.class
        );
    }

    private Transaction createPendingTransaction(TransactionRequest request, String userId) {
        Transaction transaction = new Transaction(
                request.getRemarks(),
                userId,
                request.getAmount(),
                request.getCreditCurrency(),
                request.getDebitCurrency(),
                request.getCreditAccountNumber(),
                request.getDebitAccountNumber()
        );
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;
    }

    private void updateTransactionStatus(Transaction transaction, TransactionStatus status) {
        transaction.setStatus(status);
        transactionRepository.save(transaction);
    }

    private void publishTransactionEvent(Transaction transaction) {
        TransactionEvent event = new TransactionEvent(
                transaction.getId(),
                transaction.getDebitAccountNumber(),
                transaction.getCreditAccountNumber(),
                transaction.getAmount(),
                transaction.getDebitCurrency(),
                transaction.getCreditCurrency(),
                transaction.getUserId(),
                LocalDateTime.now()
        );
        transactionProducer.sendTransactionInitiatedEvent(event);
    }

    private void handleApiError(Transaction transaction, ApiClientException e) {
        log.error("Payment service validation failed for transaction {}: {}",
                transaction.getId(), e.getMessage());
        updateTransactionStatus(transaction, TransactionStatus.FAILED);
    }

    protected boolean paymentServiceValidationFallback(Transaction transaction, String userId, Throwable t) {
        log.warn("Payment service fallback triggered for transaction {}: {}",
                transaction.getId(), t.getMessage());
        updateTransactionStatus(transaction, TransactionStatus.PENDING);
        return false;
    }

    public boolean userServiceValidationFallback(String userName, Throwable t) {
        log.warn("Fallback triggered for user {}: {}", userName, t.toString());
        throw new ApiClientException("User is invalid", HttpStatus.UNAUTHORIZED);
    }

    public void updateProcessedTransactionStatus(UUID transactionId, String transactionStatus) {
        transactionRepository.findById(transactionId).ifPresentOrElse(transaction -> {
            if ("COMPLETED".equalsIgnoreCase(transactionStatus)) {
                transaction.setStatus(TransactionStatus.COMPLETED);
            } else if ("REJECTED".equalsIgnoreCase(transactionStatus)) {
                transaction.setStatus(TransactionStatus.REJECTED);
            } else {
                transaction.setStatus(TransactionStatus.FAILED);
            }
            transactionRepository.save(transaction);
        }, () -> {
            log.warn("Transaction with ID {} not found for status update", transactionId);
        });
    }
}
