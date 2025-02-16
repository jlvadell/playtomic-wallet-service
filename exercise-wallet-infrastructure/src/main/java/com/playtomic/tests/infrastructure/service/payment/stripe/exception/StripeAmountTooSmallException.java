package com.playtomic.tests.infrastructure.service.payment.stripe.exception;

public class StripeAmountTooSmallException extends StripeServiceException {
    public static final String REASON = "Amount too small";
    public StripeAmountTooSmallException() {
        super(REASON);
    }
}
