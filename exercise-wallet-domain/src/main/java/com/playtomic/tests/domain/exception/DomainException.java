package com.playtomic.tests.domain.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    public final String details;

    public DomainException(String message, String details, Throwable cause) {
        super(message, cause);
        this.details = details;
    }
}
