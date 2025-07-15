package com.bankly.transactionservice.exception;


import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(TransactionValidationException.class)
    public ProblemDetail handleLoginException(TransactionValidationException ex){
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED,ex.getMessage());

        problemDetail.setTitle("Transaction Failed");
        problemDetail.setType(URI.create("https://api.bankly.com/errors/login-auth"));
        return problemDetail;
    }


}
