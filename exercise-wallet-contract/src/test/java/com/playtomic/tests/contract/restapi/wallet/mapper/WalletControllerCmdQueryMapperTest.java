package com.playtomic.tests.contract.restapi.wallet.mapper;

import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;
import com.playtomic.tests.application.query.wallet.query.FindWalletByIdQuery;
import com.playtomic.tests.contract.restapi.wallet.model.CurrencyAmountDto;
import com.playtomic.tests.contract.restapi.wallet.model.TransactionRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WalletControllerCmdQueryMapperTest {

    WalletControllerCmdQueryMapper mapper = WalletControllerCmdQueryMapper.INSTANCE;

    @Test
    @DisplayName("toCommand should map TransactionRequestDto to CreateTransactionCommand")
    void toCommand_shouldMapTransactionRequestDto_toCreateTransactionCommand() {
        // Given
        var userId = "userId";
        var walletId = "walletId";
        var transactionRequestDto = new TransactionRequestDto()
                .card("tokenizedCardId")
                .amount(new CurrencyAmountDto()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                );
        var expected = CreateTransactionCommand.builder()
                .walletId("walletId")
                .userId("userId")
                .tokenizedCardId("tokenizedCardId")
                .currencyValue(100)
                .currencyDecimal(2)
                .currency("EUR")
                .build();
        // When
        var actual = mapper.toCommand(userId, walletId, transactionRequestDto);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("toQuery should map userId and walletId to FindWalletByIdQuery")
    void toQuery_shouldMapUserId_andWalletId_toFindWalletByIdQuery() {
        // Given
        var userId = "userId";
        var walletId = "walletId";
        var expected = FindWalletByIdQuery.builder()
                .userId("userId")
                .walletId("walletId")
                .build();
        // When
        var actual = mapper.toQuery(userId, walletId);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}