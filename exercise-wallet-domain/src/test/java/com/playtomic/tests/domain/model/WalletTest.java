package com.playtomic.tests.domain.model;

import com.playtomic.tests.domain.exception.UnprocessableTransactionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.fiftyEuros;
import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.hundredFiftyEuros;
import static com.playtomic.tests.domain.model.fixture.WalletFixtures.walletWithHundredEuros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("applyTransaction should return a new wallet with the balance updated on success")
    void applyTransaction_shouldReturnNewWalletWithUpdatedBalance_onSuccess() {
        // Given
        var wallet = walletWithHundredEuros();
        var transaction = Transaction.builder()
                .id("T1")
                .amount(fiftyEuros())
                .build();
        // When
        var actual = wallet.applyTransaction(transaction);
        // Then
        assertThat(actual).isNotSameAs(wallet);
        assertThat(actual.getBalance()).isEqualTo(wallet.getBalance().add(transaction.getAmount()));
    }

    @Test
@DisplayName("applyTransaction should throw an exception when the transaction is negative and the balance is not enough")
    void applyTransaction_shouldThrowException_whenTransactionIsNegativeAndBalanceIsNotEnough() {
        // Given
        var wallet = walletWithHundredEuros();
        var transaction = Transaction.builder()
                .id("T1")
                .amount(hundredFiftyEuros().toBuilder()
                        .value(-15000)
                        .build())
                .build();
        // When-Then
        assertThatThrownBy(() -> wallet.applyTransaction(transaction))
                .isInstanceOf(UnprocessableTransactionException.class)
                .hasMessage("Insufficient funds");
    }


}