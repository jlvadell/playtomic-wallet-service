package com.playtomic.tests.model;

public enum TransactionStatus {
    PENDING,
    CONFIRMED,
    DECLINED,
    REFUNDED;

    public boolean isPending() {
        return this == PENDING;
    }
}
