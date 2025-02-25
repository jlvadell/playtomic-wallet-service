package com.playtomic.tests.exception;

public class UnauthorizedUserException extends BaseException {
    public UnauthorizedUserException(String message, String details) {
        super(message, details);
    }
}
