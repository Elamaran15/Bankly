package com.bankly.transactionservice.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@AllArgsConstructor
@Data
@Builder
public class TransactionEvent {

    private UUID transactionId;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private BigDecimal amount;
    private String debitCurrency;
    private String creditCurrency;
    private String userId;
    private LocalDateTime timestamp;

    public TransactionEvent() {
    }




}
