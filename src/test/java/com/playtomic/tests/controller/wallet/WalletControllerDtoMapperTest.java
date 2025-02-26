package com.playtomic.tests.controller.wallet;

import com.playtomic.tests.controller.wallet.model.TransactionRequestDto;
import com.playtomic.tests.controller.wallet.model.CurrencyAmountDto;
import com.playtomic.tests.controller.wallet.model.TransactionDto;
import com.playtomic.tests.controller.wallet.model.WalletDto;
import com.playtomic.tests.model.CurrencyAmount;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.TransactionStatus;
import com.playtomic.tests.model.Wallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class WalletControllerDtoMapperTest {

    WalletControllerDtoMapper mapper = WalletControllerDtoMapper.INSTANCE;

    @Test
    @DisplayName("toDto should map Transaction to TransactionDto")
    void toDto_shouldMapTransaction_toTransactionDto() {
        // Given
        var transaction = Transaction.builder()
                .id("id")
                .walletId("walletId")
                .tokenizedCardId("tokenizedCardId")
                .externalId("externalId")
                .amount(CurrencyAmount.builder()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .status(TransactionStatus.CONFIRMED)
                .build();

        var expected = new TransactionDto()
                .id("id")
                .status("CONFIRMED")
                .amount(new CurrencyAmountDto()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                );
        // When
        var actual = mapper.toDto(transaction);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("toDto should map Wallet to WalletDto")
    void toDto_shouldMapWallet_toWalletDto() {
        // Given
        var wallet = Wallet.builder()
                .id("id")
                .userId("userId")
                .balance(CurrencyAmount.builder()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();
        var expected = new WalletDto()
                .id("id")
                .balance(new CurrencyAmountDto()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                );
        // When
        var actual = mapper.toDto(wallet);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("toDomain should map TransactionRequestDto to Transaction")
    void toDomain_shouldMapTransactionRequestDto_toTransaction() {
        // Given
        var transactionRequestDto = new TransactionRequestDto()
                .card("tokenizedCardId")
                .amount(new CurrencyAmountDto()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                );
        var expected = Transaction.builder()
                .userId("userId")
                .walletId("walletId")
                .tokenizedCardId("tokenizedCardId")
                .amount(CurrencyAmount.builder()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .status(TransactionStatus.PENDING)
                .build();
        // When
        var actual = mapper.toDomain("userId", "walletId", transactionRequestDto);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("toDomain should map CurrencyAmountDto to CurrencyAmount")
    void toDomain_shouldMapCurrencyAmountDto_toCurrencyAmount() {
        // Given
        var currencyAmountDto = new CurrencyAmountDto()
                .value(100)
                .decimal(2)
                .currency("EUR");
        var expected = CurrencyAmount.builder()
                .value(100)
                .decimal(2)
                .currency("EUR")
                .build();
        // When
        var actual = mapper.toDomain(currencyAmountDto);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("toDto should map CurrencyAmount to CurrencyAmountDto")
    void toDto_shouldMapCurrencyAmount_toCurrencyAmountDto() {
        // Given
        var currencyAmount = CurrencyAmount.builder()
                .value(100)
                .decimal(2)
                .currency("EUR")
                .build();
        var expected = new CurrencyAmountDto()
                .value(100)
                .decimal(2)
                .currency("EUR");
        // When
        var actual = mapper.toDto(currencyAmount);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}