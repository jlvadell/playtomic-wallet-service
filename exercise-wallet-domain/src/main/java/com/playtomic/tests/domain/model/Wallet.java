package com.playtomic.tests.domain.model;

import com.playtomic.tests.domain.exception.UnprocessableTransactionException;
import lombok.Builder;
import lombok.Data;
import lombok.Value;


@Data
@Value
@Builder(toBuilder = true)
public class Wallet {
    String id;
    String userId;
    CurrencyAmount balance;

    public boolean hasSufficientFunds(CurrencyAmount amountToSubtract) {
        balance.checkCurrencyCompatibility(amountToSubtract);
        return balance.getValue() >= amountToSubtract.getAbsoluteValue();
    }

    public Wallet applyTransaction(Transaction transaction) {
        if (transaction.getAmount().isNegative() && !hasSufficientFunds(transaction.getAmount())) {
            throw new UnprocessableTransactionException("Insufficient funds", "Cannot process negative transaction");
        }
        return this.toBuilder()
                .balance(this.balance.add(transaction.getAmount()))
                .build();
    }


}
