package com.bankly.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentValidationRequest {
    private UUID transactionId;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private BigDecimal amount;
    private String debitCurrency;
    private String creditCurrency;
    private String userName;



}
