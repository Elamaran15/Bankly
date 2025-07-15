package com.bankly.transactionservice.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private String debitCurrency;
    private String creditCurrency;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private String userId; // User who initiated the transaction
    private LocalDateTime transactionDate;
    private String remarks;

    public Transaction(){

    }


    public Transaction(String remarks,String userId, BigDecimal amount, String creditCurrency, String debitCurrency, String creditAccountNumber, String debitAccountNumber) {
        this.remarks = remarks;
        this.userId = userId;
        this.amount = amount;
        this.creditCurrency = creditCurrency;
        this.debitCurrency = debitCurrency;
        this.creditAccountNumber = creditAccountNumber;
        this.debitAccountNumber = debitAccountNumber;
    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public void setDebitAccountNumber(String debitAccountNumber) {
        this.debitAccountNumber = debitAccountNumber;
    }

    public String getCreditAccountNumber() {
        return creditAccountNumber;
    }

    public void setCreditAccountNumber(String creditAccountNumber) {
        this.creditAccountNumber = creditAccountNumber;
    }

    public String getDebitCurrency() {
        return debitCurrency;
    }

    public void setDebitCurrency(String debitCurrency) {
        this.debitCurrency = debitCurrency;
    }

    public String getCreditCurrency() {
        return creditCurrency;
    }

    public void setCreditCurrency(String creditCurrency) {
        this.creditCurrency = creditCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



}
