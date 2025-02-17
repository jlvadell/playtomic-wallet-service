package com.playtomic.tests.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.fiftyEuros;
import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.negativeFiftyEuros;
import static com.playtomic.tests.domain.model.fixture.TransactionFixtures.baseTransaction;
import static com.playtomic.tests.domain.model.fixture.TransactionFixtures.pendingCardTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionTest {

    @Test
    @DisplayName("requirePaymentProcessing should return true when transaction carries card information")
    void requirePaymentProcessing_shouldReturnTrue_whenTransactionCarriesCardInformation() {
        // Given
        var transaction = pendingCardTransaction();
        // When
        var actual = transaction.requirePaymentProcessing();
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("requirePaymentProcessing should return false when transaction doesn't include card information")
    void requirePaymentProcessing_shouldReturnFalse_whenTransactionDoesNotHaveCardInformation() {
        // Given
        var transaction = baseTransaction()
                .status(TransactionStatus.PENDING)
                .build();
        // When
        var actual = transaction.requirePaymentProcessing();
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("requireBalanceCheck should return true when transaction amount is negative")
    void requireBalanceCheck_shouldReturnTrue_whenTransactionAmountIsNegative() {
        // Given
        var transaction = baseTransaction()
                .amount(negativeFiftyEuros())
                .build();
        // When
        var actual = transaction.requireBalanceCheck();
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("requireBalanceCheck should return false when transaction amount is positive")
    void requireBalanceCheck_shouldReturnFalse_whenTransactionAmountIsPositive() {
        // Given
        var transaction = baseTransaction()
                .amount(fiftyEuros())
                .build();
        // When
        var actual = transaction.requireBalanceCheck();
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("updateExternalId should return new transaction with updated external id")
    void updateExternalId_shouldReturnTransactionWithUpdatedExternalId() {
        // Given
        var transaction = baseTransaction()
                .build();
        var expected = transaction.toBuilder()
                .externalId("E1")
                .build();
        // When
        var actual = transaction.updateExternalId("E1");
        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("confirmCardPayment should return confirmed transaction when card transaction is processed")
    void confirmCardPayment_shouldReturnNewTransactionWithStatusConfirmed_whenCardTransactionIsProcessed() {
        // Given
        var transaction = pendingCardTransaction().toBuilder()
                .externalId("E1")
                .build();
        var expected = transaction.toBuilder()
                .status(TransactionStatus.CONFIRMED)
                .build();
        // When
        var actual = transaction.confirmCardPayment();
        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("confirmCardPayment should throw exception when transaction does not require card payment")
    void confirmCardPayment_shouldThrowException_whenTransactionDoesNotRequireCardPayment() {
        // Given
        var transaction = baseTransaction()
                .build();
        // When-Then
        assertThatThrownBy(transaction::confirmCardPayment)
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Transaction does not require card confirmation");
    }

    @Test
    @DisplayName("confirmCardPayment should throw exception when transaction has not been processed")
    void confirmCardPayment_shouldThrowException_whenTransactionHasNotBeenProcessed() {
        // Given
        var transaction = pendingCardTransaction().toBuilder()
                .externalId(null)
                .build();
        // When-Then
        assertThatThrownBy(transaction::confirmCardPayment)
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Transaction has not been processed");
    }

    @Test
    @DisplayName("confirm should throw exception when transaction is not pending")
    void confirm_shouldThrowException_whenTransactionIsNotPending() {
        // Given
        var transaction = baseTransaction()
                .status(TransactionStatus.CONFIRMED)
                .build();
        // When-Then
        assertThatThrownBy(transaction::confirm)
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Transaction is not awaiting confirmation");
    }

    @Test
    @DisplayName("confirm should return confirmed transaction when transaction is pending")
    void confirm_shouldReturnNewTransactionWithStatusConfirmed_whenTransactionIsPending() {
        // Given
        var transaction = baseTransaction()
                .build();
        var expected = transaction.toBuilder()
                .status(TransactionStatus.CONFIRMED)
                .build();
        // When
        var actual = transaction.confirm();
        // Then
        assertThat(actual).isEqualTo(expected);
    }
}
