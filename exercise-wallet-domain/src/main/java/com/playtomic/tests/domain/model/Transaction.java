package com.playtomic.tests.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder(toBuilder = true)
public class Transaction {
    String id;
    String walletId;
    String tokenizedCardId;
    String externalId;
    CurrencyAmount amount;
    TransactionStatus status;


    public boolean requirePaymentProcessing() {
        return tokenizedCardId != null;
    }

    public boolean requireBalanceCheck() {
        return amount.isNegative();
    }

    public Transaction updateExternalId(String externalId) {
        return Transaction.builder()
                .id(id)
                .walletId(walletId)
                .tokenizedCardId(tokenizedCardId)
                .externalId(externalId)
                .amount(amount)
                .status(status)
                .build();
    }

    public Transaction updateId(String id) {
        return Transaction.builder()
                .id(id)
                .walletId(walletId)
                .tokenizedCardId(tokenizedCardId)
                .externalId(externalId)
                .amount(amount)
                .status(status)
                .build();
    }

    public Transaction confirmCardPayment() {
        verifyCardPayment();
        return confirm();
    }

    public Transaction confirm() {
        verifyConfirmationRequirements();
        return Transaction.builder()
                .id(id)
                .walletId(walletId)
                .tokenizedCardId(tokenizedCardId)
                .externalId(externalId)
                .amount(amount)
                .status(TransactionStatus.CONFIRMED)
                .build();
    }

    private void verifyConfirmationRequirements() {
        if (!status.isPending()) {
            throw new IllegalStateException("Transaction is not awaiting confirmation");
        }
    }

    private void verifyCardPayment() {
        if (tokenizedCardId == null) {
            throw new IllegalStateException("Transaction does not require card confirmation");
        }
        if (externalId == null) {
            throw new IllegalStateException("Transaction has not been processed");
        }
    }
}
