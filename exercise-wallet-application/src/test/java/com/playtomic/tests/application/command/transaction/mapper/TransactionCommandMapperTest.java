package com.playtomic.tests.application.command.transaction.mapper;

import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;
import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.domain.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionCommandMapperTest {

    TransactionCommandMapper transactionCommandMapper = TransactionCommandMapper.INSTANCE;

    @Test
    @DisplayName("Should map CreateTransactionCommand to Transaction")
    void toDomain_shouldMapCorrectly() {
        // Given
        var command = CreateTransactionCommand.builder()
                .walletId("walletId")
                .tokenizedCardId("tokenizedCardId")
                .currencyValue(100)
                .currencyDecimal(2)
                .currency("EUR")
                .build();

        var expected = Transaction.builder()
                .walletId("walletId")
                .tokenizedCardId("tokenizedCardId")
                .amount(CurrencyAmount.builder()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();

        // When
        var actual = transactionCommandMapper.toDomain(command);

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Should map CreateTransactionCommand to CurrencyAmount")
    void toDomainCurrencyAmount_shouldMapCorrectly() {
        // Given
        var command = CreateTransactionCommand.builder()
                .currencyValue(100)
                .currencyDecimal(2)
                .currency("EUR")
                .build();

        var expected = CurrencyAmount.builder()
                .value(100)
                .decimal(2)
                .currency("EUR")
                .build();

        // When
        var actual = transactionCommandMapper.toDomainCurrencyAmount(command);

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}