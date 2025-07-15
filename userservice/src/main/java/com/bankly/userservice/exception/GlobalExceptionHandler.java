package com.bankly.userservice.exception;


import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleLoginException(LoginAuthException ex){
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED,ex.getMessage());

        problemDetail.setTitle("Login Authentication Failed");
        problemDetail.setType(URI.create("https://api.yourbank.com/errors/login-auth"));

        // 👇 Custom field: errorCode
        problemDetail.setProperty("errorCode", ex.getErrorCode());

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.yourbank.com/errors/internal-server-error"));
        return problemDetail;
    }
}
