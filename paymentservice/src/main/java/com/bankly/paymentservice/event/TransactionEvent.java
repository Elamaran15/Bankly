package com.bankly.paymentservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionEvent {

    private UUID transactionId;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private BigDecimal amount;
    private String debitCurrency;
    private String creditCurrency;
    private String userId;
    private LocalDateTime timestamp;

}
