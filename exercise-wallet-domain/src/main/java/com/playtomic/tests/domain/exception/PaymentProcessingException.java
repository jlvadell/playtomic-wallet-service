package com.playtomic.tests.domain.exception;

public class PaymentProcessingException extends DomainException {

    public PaymentProcessingException(String message, String reason, Throwable cause) {
        super(message, reason, cause);
    }
}
