package com.playtomic.tests.application.exception;

public class ApplicationException extends RuntimeException{

    public final String details;

    public ApplicationException(String message, String details) {
        super(message);
        this.details = details;
    }
}
