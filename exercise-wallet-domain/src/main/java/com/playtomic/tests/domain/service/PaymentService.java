package com.playtomic.tests.domain.service;

import com.playtomic.tests.domain.model.Transaction;

import java.util.Optional;

public interface PaymentService {

    Optional<Transaction> chargeTransaction(Transaction transaction);

    Optional<Transaction> refundTransaction(Transaction transaction);

}
