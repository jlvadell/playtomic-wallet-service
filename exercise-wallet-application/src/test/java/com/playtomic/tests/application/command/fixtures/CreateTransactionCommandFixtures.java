package com.playtomic.tests.application.command.fixtures;

import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;

public class CreateTransactionCommandFixtures {
    public static CreateTransactionCommand.CreateTransactionCommandBuilder baseCreateTransactionCommand() {
        return CreateTransactionCommand.builder()
                .walletId("W1")
                .userId("U1");
    }

    public static CreateTransactionCommand hundredEurosCardTransactionCommand() {
        return baseCreateTransactionCommand()
                .currencyValue(10000)
                .currencyDecimal(2)
                .currency("EUR")
                .tokenizedCardId("T1")
                .build();
    }

    public static CreateTransactionCommand negativeHundredEurosTransactionCommand() {
        return baseCreateTransactionCommand()
                .currencyValue(-10000)
                .currencyDecimal(2)
                .currency("EUR")
                .tokenizedCardId(null)
                .build();
    }

    public static CreateTransactionCommand negativeFiftyEurosTransactionCommand() {
        return baseCreateTransactionCommand()
                .currencyValue(-5000)
                .currencyDecimal(2)
                .currency("EUR")
                .tokenizedCardId(null)
                .build();
    }

}
