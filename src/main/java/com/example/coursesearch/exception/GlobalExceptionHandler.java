package com.example.coursesearch.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {

        log.error("Type mismatch error: {}", e.getMessage());
        
        Map<String, Object> error = Map.of(

            "error", "Invalid parameter type",
            "message", "Parameter '" + e.getName() + "' should be of type " + e.getRequiredType().getSimpleName(),
            "timestamp", Instant.now()

        );
        
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {

        log.error("Illegal argument error: {}", e.getMessage());
        
        Map<String, Object> error = Map.of(
            "error", "Invalid argument",
            "message", e.getMessage(),
            "timestamp", Instant.now()
        );
        
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {

        log.error("Unexpected error: {}", e.getMessage(), e);
        
        Map<String, Object> error = Map.of(
            "error", "Internal server error",
            "message", "An unexpected error occurred",
            "timestamp", Instant.now()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}