package com.example.jokes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCountException.class)
    public ResponseEntity<String> handleInvalidCountException(InvalidCountException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Add more exception handlers if needed
}
