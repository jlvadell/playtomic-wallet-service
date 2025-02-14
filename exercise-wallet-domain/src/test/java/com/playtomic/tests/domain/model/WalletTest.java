package com.playtomic.tests.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.fiftyEuros;
import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.hundredFiftyEuros;
import static com.playtomic.tests.domain.model.fixture.WalletFixtures.walletWithHundredEuros;
import static org.assertj.core.api.Assertions.assertThat;

class WalletTest {

    @Test
    @DisplayName("hasSufficientFunds should return true when balance is enough")
    void hasSufficientFunds_shouldReturnTrue_whenBalanceIsEnough() {
        // Given
        var wallet = walletWithHundredEuros();
        var amountToSubtract = fiftyEuros();
        // When
        var actual = wallet.hasSufficientFunds(amountToSubtract);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("hasSufficientFunds should return false when balance is not enough")
    void hasSufficientFunds_shouldReturnFalse_whenBalanceIsNotEnough() {
        // Given
        var wallet = walletWithHundredEuros();
        var amountToSubtract = hundredFiftyEuros();
        // When
        var actual = wallet.hasSufficientFunds(amountToSubtract);
        // Then
        assertThat(actual).isFalse();
    }

}