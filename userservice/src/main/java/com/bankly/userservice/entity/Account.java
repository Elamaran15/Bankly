package com.bankly.userservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="account")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {

    private AccountType accountType; public Account(String accountNumber, BigDecimal balance, Users user) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.user = user;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String accountNumber;
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public Account(String accountNumber, BigDecimal zero, Users user, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.user = user;
        this.accountType=accountType;
        this.balance=zero;
    }
}
