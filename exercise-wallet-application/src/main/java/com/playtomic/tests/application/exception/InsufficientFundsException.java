package com.playtomic.tests.application.exception;

public class InsufficientFundsException extends ApplicationException {
    public InsufficientFundsException(String message, String details) {
        super(message, details);
    }
}
