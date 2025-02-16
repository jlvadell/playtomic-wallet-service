package com.playtomic.tests.domain.exception;

public class UnprocessableTransactionException extends DomainException{

    public UnprocessableTransactionException(String message, String details) {
        super(message, details, null);
    }
}
