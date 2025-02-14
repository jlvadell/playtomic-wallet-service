package com.playtomic.tests.application.query;

public interface QueryHandler<Q extends Query, R> {
    R handle(Q query);
}
