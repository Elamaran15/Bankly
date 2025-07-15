package com.bankly.paymentservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID transactionId; // Link to the original transaction
    private BigDecimal convertedAmount;
    private TransactionType transactionType;
    private String transactionStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime processDate;
    private String userName;
}
