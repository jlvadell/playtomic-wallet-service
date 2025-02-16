package com.playtomic.tests.application.query.wallet.query;

import com.playtomic.tests.application.query.Query;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder(toBuilder = true)
public class FindWalletByIdQuery implements Query {
    @NotNull
    String walletId;
    @NotNull
    String userId;
}
