package com.playtomic.tests.application.exception;

public class InternalUnexpectedException extends ApplicationException {
    public InternalUnexpectedException(String message, String details) {
        super(message, details);
    }
}
