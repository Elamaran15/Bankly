package com.bankly.userservice.exception;

import lombok.Getter;


public class LoginAuthException extends RuntimeException{

    private final String errorCode;

    public LoginAuthException(String message,String errorCode){
        super(message);
        this.errorCode=errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
