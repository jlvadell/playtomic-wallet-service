package com.playtomic.tests.application.validation.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotZeroValidatorTest {

    NotZeroValidator validator = new NotZeroValidator();

    @Test
    @DisplayName("Should return true when value is positive")
    void isValid_shouldReturnTrue_whenValueIsNotZero() {
        // Given
        var value = 1;

        // When
        var actual = validator.isValid(value, null);

        // Then
        assertTrue(actual);
    }

    @Test
    @DisplayName("Should return true when value is negative")
    void isValid_shouldReturnTrue_whenValueIsNegative() {
        // Given
        var value = -1;

        // When
        var actual = validator.isValid(value, null);

        // Then
        assertTrue(actual);
    }

    @Test
    @DisplayName("Should return false when value is zero")
    void isValid_shouldReturnFalse_whenValueIsZero() {
        // Given
        var value = 0;

        // When
        var actual = validator.isValid(value, null);

        // Then
        assertFalse(actual);
    }

    @Test
    @DisplayName("Should return true when value is null")
    void isValid_shouldReturnTrue_whenValueIsNull() {
        // Given
        Integer value = null;

        // When
        var actual = validator.isValid(value, null);

        // Then
        assertTrue(actual);
    }

}