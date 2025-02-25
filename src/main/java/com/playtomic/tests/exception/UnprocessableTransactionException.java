package com.playtomic.tests.exception;

public class UnprocessableTransactionException extends BaseException{

    public UnprocessableTransactionException(String message, String details) {
        super(message, details, null);
    }
}
