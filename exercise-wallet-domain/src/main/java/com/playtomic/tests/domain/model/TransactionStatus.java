package com.playtomic.tests.domain.model;

public enum TransactionStatus {
    PENDING,
    CONFIRMED,
    DECLINED,
    REFUNDED;

    public boolean isPending() {
        return this == PENDING;
    }
}
