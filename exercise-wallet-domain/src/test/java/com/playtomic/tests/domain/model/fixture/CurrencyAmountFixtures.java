package com.playtomic.tests.domain.model.fixture;

import com.playtomic.tests.domain.model.CurrencyAmount;

public class CurrencyAmountFixtures {

    public static CurrencyAmount hundredEuros() {
        return CurrencyAmount.builder()
                .value(10000)
                .decimal(2)
                .currency("EUR")
                .build();
    }

    public static CurrencyAmount hundredDollars() {
        return CurrencyAmount.builder()
                .value(10000)
                .decimal(2)
                .currency("USD")
                .build();
    }

    public static CurrencyAmount fiftyEuros() {
        return CurrencyAmount.builder()
                .value(5000)
                .decimal(2)
                .currency("EUR")
                .build();
    }

    public static CurrencyAmount negativeFiftyEuros() {
        return CurrencyAmount.builder()
                .value(-5000)
                .decimal(2)
                .currency("EUR")
                .build();
    }

    public static CurrencyAmount hundredFiftyEuros() {
        return CurrencyAmount.builder()
                .value(15000)
                .decimal(2)
                .currency("EUR")
                .build();
    }
}
