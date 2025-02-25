package com.playtomic.tests.model.fixture;

import com.playtomic.tests.model.Wallet;

public class WalletFixtures {
    public static Wallet walletWithHundredEuros() {
        return Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(CurrencyAmountFixtures.hundredEuros())
                .build();
    }

    public static Wallet walletWithFiftyEuros() {
        return Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(CurrencyAmountFixtures.fiftyEuros())
                .build();
    }

}
