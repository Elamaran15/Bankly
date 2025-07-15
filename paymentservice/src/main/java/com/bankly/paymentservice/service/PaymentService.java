package com.bankly.paymentservice.service;


import com.bankly.paymentservice.dto.PaymentValidationRequest;
import com.bankly.paymentservice.entity.ExchangeRate;
import com.bankly.paymentservice.entity.PaymentLog;
import com.bankly.paymentservice.entity.TransactionType;
import com.bankly.paymentservice.event.TransactionEvent;
import com.bankly.paymentservice.event.TransactionSuccessEvent;
import com.bankly.paymentservice.kafka.TransactionProcessedProducer;
import com.bankly.paymentservice.repository.ExchangeRateRepository;
import com.bankly.paymentservice.repository.PaymentLogRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentLogRepository paymentLogRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final TransactionProcessedProducer transactionProcessedProducer;
    public PaymentService(PaymentLogRepository paymentLogRepository, ExchangeRateRepository exchangeRateRepository, TransactionProcessedProducer transactionProcessedProducer) {
        this.paymentLogRepository = paymentLogRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.transactionProcessedProducer = transactionProcessedProducer;
    }

    // Synchronous validation for Transaction Service
    public boolean validatePayment(PaymentValidationRequest request, String userName) { // Changed userIdFromJwt to userName
        System.out.println("Validating payment for transaction ID: " + request.getTransactionId() + " by user: " + userName);
       BigDecimal convertedAmount=getConvertedAmount(request.getAmount(),request.getDebitCurrency(),request.getCreditCurrency());
        // Simulate various business rules and validations
        if (convertedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            logPaymentAttempt(request, userName, "FAILED", "Invalid amount");
            return false; // Amount must be positive
        }
        if (convertedAmount.compareTo(new BigDecimal("100000")) > 0) {
            logPaymentAttempt(request, userName, "FAILED", "Amount exceeds daily limit");
            return false; // Simulate a daily limit
        }
        // Simulate checking account balances (would typically involve another service call)
        // For demo, assume sufficient funds
        if ("ACC-INVALID".equals(request.getDebitAccountNumber())) {
            logPaymentAttempt(request, userName, "FAILED", "Invalid source account");
            return false;
        }

        // Log successful validation
        logPaymentAttempt(request, userName, "INITIATED", "Initial validation passed");
        return true;
    }

    // Asynchronous processing triggered by Kafka event
    public void processPaymentAsync(TransactionEvent event) {
        System.out.println("Asynchronously processing payment for transaction ID: " + event.getTransactionId());

        // Simulate complex payment processing logic
        try {
            Thread.sleep(2000); // Simulate a long-running process
            // Update the payment log with processing status
            PaymentLog log = PaymentLog.builder().paymentDate(LocalDateTime.now())
                            .convertedAmount(getConvertedAmount(event.getAmount(),event.getDebitCurrency(),event.getCreditCurrency()))
                                    .transactionId(event.getTransactionId())
                                            .transactionStatus("COMPLETED")
                                                    .processDate(LocalDateTime.now())
                                                            .userName(event.getUserId())
                                                                    .transactionType(getTransactionType(event.getCreditCurrency(),event.getDebitCurrency()))
                                                                            .build();
            paymentLogRepository.save(log);
            System.out.println("Payment processing completed for transaction ID: " + event.getTransactionId());

            transactionProcessedProducer.sendTransactionProcessedEvent(TransactionSuccessEvent.builder()
                    .transactionStatus("COMPLETED").transactionId(event.getTransactionId()).build());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Payment processing interrupted for transaction ID: " + event.getTransactionId());
            // Log failure
            PaymentLog log = paymentLogRepository.findByTransactionId(event.getTransactionId())
                    .orElse(getPaymentLog(event, "FAILED"));
            paymentLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Error during async payment processing for transaction ID: " + event.getTransactionId() + ": " + e.getMessage());
            // Log failure
            PaymentLog log = paymentLogRepository.findByTransactionId(event.getTransactionId())
                    .orElse(getPaymentLog(event, "FAILED"));
            paymentLogRepository.save(log);
        }
    }

    private PaymentLog getPaymentLog(TransactionEvent event, String transactionStatus) {
        return PaymentLog.builder().paymentDate(LocalDateTime.now())
                .convertedAmount(getConvertedAmount(event.getAmount(), event.getDebitCurrency(), event.getCreditCurrency()))
                .transactionId(event.getTransactionId())
                .transactionStatus(transactionStatus)
                .processDate(LocalDateTime.now())
                .build();
    }

    private TransactionType getTransactionType(String creditCurrency, String debitCurrency) {
        if(creditCurrency.equalsIgnoreCase(debitCurrency))
            return TransactionType.DOMESTIC_FUND_TRANSFER;
        else
            return TransactionType.CROSS_BORDER_FUND_TRANSFER;
    }

    private BigDecimal getConvertedAmount(BigDecimal amount, String debitCurrency, String creditCurrency) {
        if (debitCurrency.equalsIgnoreCase(creditCurrency)) {
            return amount;
        }

        ExchangeRate rate = exchangeRateRepository.findByFromCurrencyAndToCurrency(debitCurrency.toUpperCase(), creditCurrency.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Exchange rate not found from " + debitCurrency + " to " + creditCurrency));

        return amount.multiply(rate.getRate());

    }

    private void logPaymentAttempt(PaymentValidationRequest request, String userId, String validationStatus, String remarks) {
        PaymentLog log = paymentLogRepository.findByTransactionId(request.getTransactionId())
                .orElse(getPaymentLog(request, validationStatus));
        paymentLogRepository.save(log);
    }

    private PaymentLog getPaymentLog(PaymentValidationRequest request, String validationStatus) {
        return PaymentLog.builder().paymentDate(LocalDateTime.now())
        .convertedAmount(getConvertedAmount(request.getAmount(), request.getDebitCurrency(), request.getCreditCurrency()))
        .transactionId(request.getTransactionId())
        .transactionStatus(validationStatus)
        .processDate(LocalDateTime.now())
        .build();
    }


}
