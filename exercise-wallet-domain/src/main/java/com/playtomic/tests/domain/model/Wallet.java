package com.playtomic.tests.domain.model;

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


}
