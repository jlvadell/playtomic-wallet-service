package com.playtomic.tests.exception;

public class InternalUnexpectedException extends BaseException {
    public InternalUnexpectedException(String message, String details) {
        super(message, details);
    }
    public InternalUnexpectedException(String message, String details, Throwable cause) {
        super(message, details, cause);
    }
}
