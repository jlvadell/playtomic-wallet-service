package com.playtomic.tests.infrastructure.persistence.mongodb.exception;

public class TransactionErrorException extends RuntimeException{

    public TransactionErrorException(String message) {
        super(message);
    }
}
