package com.example.gateway.exception;

public class BadConfigurationException extends RuntimeException {

    public BadConfigurationException(String message) {
        super(message);
    }
}
