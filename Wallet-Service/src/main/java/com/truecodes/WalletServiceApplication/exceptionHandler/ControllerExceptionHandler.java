package com.truecodes.WalletServiceApplication.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);


    @ExceptionHandler(ClientSideAPIRequestException.class)
    public ResponseEntity<Map<String, Object>> handleCustomBadRequest(ClientSideAPIRequestException ex) {
        ex.printStackTrace();
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", Instant.now().toEpochMilli());
        errorBody.put("error", ex.getStatus());
        errorBody.put("message", ex.getMessage());
        System.out.println("Error Body: " + errorBody);
        return new ResponseEntity<>(errorBody, ex.getStatus());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", Instant.now().toEpochMilli());
        errorBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorBody.put("error", "Internal Server Error");
        errorBody.put("message", ex.getMessage());
        return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
