package com.playtomic.tests.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyAmountTest {

    @Test
    @DisplayName("add should return correct value")
    void add_shouldReturnCorrectValue() {
        // Given
        var hundredEuros = hundredEuros();
        var fiftyEuros = fiftyEuros();
        var expected = hundredFiftyEuros();
        // When
        var actual = hundredEuros.add(fiftyEuros);
        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("add should throw exception when currencies do not match")
    void add_shouldThrowException_whenCurrenciesDoesNotMatch() {
        // Given
        var hundredEuros = hundredEuros();
        var hundredDollars = hundredDollars();
        // When-Then
        assertThatThrownBy(() -> hundredEuros.add(hundredDollars)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("add should throw exception when decimal do not match")
    void add_shouldThrowException_whenDecimalDoesNotMatch() {
        // Given
        var hundredEuros = hundredEuros();
        var weirdEuros = CurrencyAmount.builder()
                .value(10000)
                .decimal(3)
                .currency("EUR")
                .build();
        // When-Then
        assertThatThrownBy(() -> hundredEuros.add(weirdEuros)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("subtract should return correct value")
    void subtract_shouldReturnCorrectValue() {
        // Given
        var hundredEuros = hundredEuros();
        var fiftyEuros = fiftyEuros();
        var expected = fiftyEuros();
        // When
        var actual = hundredEuros.subtract(fiftyEuros);
        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("subtract should throw exception when currencies do not match")
    void subtract_shouldThrowException_whenCurrenciesDoesNotMatch() {
        // Given
        var hundredEuros = hundredEuros();
        var hundredDollars = hundredDollars();
        // When-Then
        assertThatThrownBy(() -> hundredEuros.subtract(hundredDollars)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("subtract should throw exception when decimal do not match")
    void subtract_shouldThrowException_whenDecimalDoesNotMatch() {
        // Given
        var hundredEuros = hundredEuros();
        var weirdEuros = CurrencyAmount.builder()
                .value(10000)
                .decimal(3)
                .currency("EUR")
                .build();
        // When-Then
        assertThatThrownBy(() -> hundredEuros.subtract(weirdEuros)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("toBigDecimal should return correct value")
    void toBigDecimal_shouldReturnCorrectValue() {
        // Given
        var hundredEuros = hundredEuros();
        var expected = new BigDecimal("100.00");
        // When
        var actual = hundredEuros.toBigDecimal();
        // Then
        assertThat(actual).isEqualTo(expected);
    }

}