package com.playtomic.tests.infrastructure.service.payment.stripe;

import com.playtomic.tests.domain.exception.PaymentProcessingException;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.service.PaymentService;
import com.playtomic.tests.infrastructure.service.payment.stripe.exception.StripeServiceException;
import com.playtomic.tests.infrastructure.service.payment.stripe.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final StripeService stripeService;

    @Override
    public Transaction chargeTransaction(Transaction transaction) {
        log.info("[PaymentServiceImpl::chargeTransaction] transaction: {}", transaction);
        try {
            Payment payment = stripeService.charge(transaction.getTokenizedCardId(), transaction.getAmount().toBigDecimal());
            return transaction.updateExternalId(payment.getId());
        } catch (StripeServiceException ex) {
            log.error("[PaymentServiceImpl::chargeTransaction] Error charging transaction: {}", transaction, ex);
            throw new PaymentProcessingException("Error charging transaction", ex.getDetails(), ex);
        }
    }

    @Override
    public Transaction refundTransaction(Transaction transaction) {
        log.info("[PaymentServiceImpl::refundTransaction] transaction: {}", transaction);
        try {
            stripeService.refund(transaction.getExternalId());
            return transaction;
        } catch (StripeServiceException ex) {
            log.error("[PaymentServiceImpl::refundTransaction] Error refunding transaction: {}", transaction, ex);
            throw new PaymentProcessingException("Error refunding transaction", ex.getDetails(), ex);
        }
    }

}
