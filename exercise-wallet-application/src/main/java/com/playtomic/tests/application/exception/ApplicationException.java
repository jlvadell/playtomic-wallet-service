package com.playtomic.tests.application.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{

    public final String details;

    public ApplicationException(String message, String details) {
        super(message);
        this.details = details;
    }
}
