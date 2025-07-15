package com.bankly.transactionservice.controller;

import com.bankly.transactionservice.dto.TransactionRequest;
import com.bankly.transactionservice.entity.Transaction;
import com.bankly.transactionservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/initiate")
    public ResponseEntity<?> initiateTransaction(@RequestBody TransactionRequest request) {
        try {
            // In a no-security setup, we need a dummy user ID for business logic.
            // In a real application, this would come from an authentication context.
            if(transactionService.validateUser(request.getUserName())) {
                Transaction transaction = transactionService.initiateTransaction(request, request.getUserName());
                return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
            }else{
                return ResponseEntity.badRequest().body("Unauthorized User");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error initiating transaction: " + e.getMessage());
        }
    }

}
