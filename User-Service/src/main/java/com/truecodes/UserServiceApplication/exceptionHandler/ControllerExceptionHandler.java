package com.truecodes.UserServiceApplication.exceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.truecodes.UserServiceApplication.exceptionHandler.ClientSideAPIRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.*;

@Component
@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMissingBodyOrBadFormat(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            if (invalidFormat.getTargetType().isEnum()) {
                String fieldName = invalidFormat.getPath().get(0).getFieldName();
                String invalidValue = invalidFormat.getValue().toString();
                Class<?> enumClass = invalidFormat.getTargetType();

                String allowed = String.join(", ",
                        Arrays.stream(enumClass.getEnumConstants())
                                .map(Object::toString)
                                .toArray(String[]::new)
                );

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Bad Request",
                                "message", String.format(
                                        "Invalid value '%s' for field '%s'. Allowed values: [%s]",
                                        invalidValue, fieldName, allowed
                                )
                        ));
            }
        }
        if (cause instanceof UnrecognizedPropertyException unrecognizedEx) {
            String unknownField = unrecognizedEx.getPropertyName();
            Collection<Object> knownProps = unrecognizedEx.getKnownPropertyIds();

            Map<String, Object> errorDetails = new LinkedHashMap<>();
            errorDetails.put("error", "Bad Request");
            errorDetails.put("message", "Unknown property found in JSON request");
            errorDetails.put("unknownField", unknownField);
            errorDetails.put("expectedFields", knownProps);
            errorDetails.put("timestamp", System.currentTimeMillis());
            errorDetails.put("status", HttpStatus.BAD_REQUEST.value());

            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> fallbackError = new LinkedHashMap<>();
        fallbackError.put("error", "Bad Request");
        fallbackError.put("message", "Malformed JSON or invalid format");
        fallbackError.put("timestamp", System.currentTimeMillis());
        fallbackError.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(fallbackError, HttpStatus.BAD_REQUEST);
    }

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
