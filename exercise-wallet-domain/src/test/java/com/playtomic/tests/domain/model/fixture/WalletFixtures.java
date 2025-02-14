package com.playtomic.tests.domain.model.fixture;

import com.playtomic.tests.domain.model.Wallet;

public class WalletFixtures {
    public static Wallet walletWithHundredEuros() {
        return Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(CurrencyAmountFixtures.hundredEuros())
                .build();
    }
}
