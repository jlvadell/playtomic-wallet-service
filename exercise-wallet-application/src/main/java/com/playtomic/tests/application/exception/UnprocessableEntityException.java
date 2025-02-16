package com.playtomic.tests.application.exception;

public class UnprocessableEntityException extends ApplicationException {
    public UnprocessableEntityException(String message, String details) {
        super(message, details);
    }
}
