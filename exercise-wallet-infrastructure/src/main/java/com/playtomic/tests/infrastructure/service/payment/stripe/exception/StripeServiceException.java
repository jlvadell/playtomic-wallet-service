package com.playtomic.tests.infrastructure.service.payment.stripe.exception;

import lombok.Getter;

@Getter
public class StripeServiceException extends RuntimeException {
    public final String details;

    public StripeServiceException(String details) {
        this.details = details;
    }
}
