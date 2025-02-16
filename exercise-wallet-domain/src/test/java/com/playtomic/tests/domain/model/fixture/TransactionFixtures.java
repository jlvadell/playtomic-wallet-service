package com.playtomic.tests.domain.model.fixture;

import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.TransactionStatus;

import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.hundredEuros;

public class TransactionFixtures {

    public static Transaction pendingCardTransaction() {
        return baseTransaction()
                .tokenizedCardId("C1")
                .build();
    }

    public static Transaction processedPendingCardTransaction() {
        return baseTransaction()
                .tokenizedCardId("C1")
                .externalId("E1")
                .build();
    }

    public static Transaction confirmedCardTransaction() {
        return baseTransaction()
                .externalId("E1")
                .tokenizedCardId("C1")
                .status(TransactionStatus.CONFIRMED)
                .build();
    }


    public static Transaction.TransactionBuilder baseTransaction() {
        return Transaction.builder()
                .id("T1")
                .walletId("W1")
                .status(TransactionStatus.PENDING)
                .amount(hundredEuros());
    }
}
