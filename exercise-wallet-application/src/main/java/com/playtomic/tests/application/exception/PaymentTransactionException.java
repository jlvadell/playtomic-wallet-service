package com.playtomic.tests.application.exception;

public class PaymentTransactionException extends ApplicationException {
    public PaymentTransactionException(String message, String details) {
        super(message, details);
    }
}
