package com.bankly.userservice.service;


import com.bankly.userservice.entity.Account;
import com.bankly.userservice.entity.AccountType;
import com.bankly.userservice.entity.Users;
import com.bankly.userservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Users user) {
        // Generate a unique account number (simplified for demo)
        String accountNumber = generateUniqueAccountNumber();
        AccountType accountType = switch (user.getAccountType()) {
            case "SA" -> AccountType.SAVINGS_ACCOUNT;
            case "LA" -> AccountType.LOAN_ACCOUNT;
            case "CA" -> AccountType.CURRENT_ACCOUNT;
            default -> throw new IllegalArgumentException("Invalid account type: " + user.getAccountType());
        };
        Account account = new Account(accountNumber, BigDecimal.ZERO, user,accountType); // Initial balance is 0
        return accountRepository.save(account);
    }

    public Optional<Account> findAccountById(UUID id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    private String generateUniqueAccountNumber() {
        // In a real application, this would involve more robust logic
        // to ensure uniqueness and compliance with banking standards.
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
