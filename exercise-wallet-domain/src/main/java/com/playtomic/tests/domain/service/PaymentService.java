package com.playtomic.tests.domain.service;

import com.playtomic.tests.domain.exception.PaymentProcessingException;
import com.playtomic.tests.domain.model.Transaction;

public interface PaymentService {

    Transaction chargeTransaction(Transaction transaction) throws PaymentProcessingException;

    Transaction refundTransaction(Transaction transaction) throws PaymentProcessingException;

}
