package com.playtomic.tests.domain.model.fixture;

import com.playtomic.tests.domain.model.CurrencyAmount;

public class CurrencyAmountFixtures {

    public static CurrencyAmount HundredEuros() {
        return CurrencyAmount.builder()
                .value(10000)
                .decimal(2)
                .currency("EUR")
                .build();
    }

    public static CurrencyAmount HundredDollars() {
        return CurrencyAmount.builder()
                .value(10000)
                .decimal(2)
                .currency("USD")
                .build();
    }

    public static CurrencyAmount FiftyEuros() {
        return CurrencyAmount.builder()
                .value(5000)
                .decimal(2)
                .currency("EUR")
                .build();
    }

    public static CurrencyAmount HundredFiftyEuros() {
        return CurrencyAmount.builder()
                .value(15000)
                .decimal(2)
                .currency("EUR")
                .build();
    }
}
