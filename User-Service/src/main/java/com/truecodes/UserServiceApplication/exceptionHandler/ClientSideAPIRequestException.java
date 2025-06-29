package com.truecodes.UserServiceApplication.exceptionHandler;


import org.springframework.http.HttpStatus;

public class ClientSideAPIRequestException extends RuntimeException {
    private final HttpStatus status;
    public ClientSideAPIRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

