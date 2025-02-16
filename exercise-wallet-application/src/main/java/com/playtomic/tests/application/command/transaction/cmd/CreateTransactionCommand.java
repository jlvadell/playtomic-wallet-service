package com.playtomic.tests.application.command.transaction.cmd;

import com.playtomic.tests.application.command.Command;
import com.playtomic.tests.application.validation.NotZero;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder(toBuilder = true)
public class CreateTransactionCommand implements Command {
    @NotNull
    String walletId;

    @NotNull
    String userId;

    @NotZero
    int currencyValue;

    @PositiveOrZero
    int currencyDecimal;

    @NotNull
    String currency;

    @Nullable
    String tokenizedCardId;

}
