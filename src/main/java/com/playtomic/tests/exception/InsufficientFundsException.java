package com.playtomic.tests.exception;

public class InsufficientFundsException extends BaseException {
    public InsufficientFundsException(String message, String details) {
        super(message, details);
    }
}
