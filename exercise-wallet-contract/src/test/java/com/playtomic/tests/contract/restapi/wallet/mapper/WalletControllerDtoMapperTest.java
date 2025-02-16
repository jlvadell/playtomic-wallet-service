package com.playtomic.tests.contract.restapi.wallet.mapper;

import com.playtomic.tests.contract.restapi.wallet.model.CurrencyAmountDto;
import com.playtomic.tests.contract.restapi.wallet.model.TransactionDto;
import com.playtomic.tests.contract.restapi.wallet.model.WalletDto;
import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.TransactionStatus;
import com.playtomic.tests.domain.model.Wallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WalletControllerDtoMapperTest {

    WalletControllerDtoMapper mapper = WalletControllerDtoMapper.INSTANCE;

    @Test
    @DisplayName("toDto should map Transaction to TransactionDto")
    void toDto_should_map_Transaction_to_TransactionDto() {
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
    void toDto_should_map_Wallet_to_WalletDto() {
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

}