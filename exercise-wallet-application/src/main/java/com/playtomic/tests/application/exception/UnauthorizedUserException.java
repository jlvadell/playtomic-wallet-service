package com.playtomic.tests.application.exception;

public class UnauthorizedUserException extends ApplicationException {
    public UnauthorizedUserException(String message, String details) {
        super(message, details);
    }
}
