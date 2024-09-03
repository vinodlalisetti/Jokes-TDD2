package com.example.jokes.exception;

public class InvalidCountException extends RuntimeException {
    public InvalidCountException(String message) {
        super(message);
    }
}
