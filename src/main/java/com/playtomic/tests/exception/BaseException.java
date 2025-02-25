package com.playtomic.tests.exception;

public class BaseException extends RuntimeException {

    public final String details;

    public BaseException(String message, String details, Throwable cause) {
        super(message, cause);
        this.details = details;
    }
}
