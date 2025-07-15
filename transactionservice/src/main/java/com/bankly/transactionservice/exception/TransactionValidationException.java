package com.bankly.transactionservice.exception;

public class TransactionValidationException extends RuntimeException{

    public TransactionValidationException(String message){
        super(message);
    }
}
