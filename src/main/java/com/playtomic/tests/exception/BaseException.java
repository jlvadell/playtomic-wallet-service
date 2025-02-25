package com.playtomic.tests.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    public final String details;

    public BaseException(String message, String details, Throwable cause) {
        super(message, cause);
        this.details = details;
    }

    public BaseException(String message, String details) {
        super(message);
        this.details = details;
    }
}
