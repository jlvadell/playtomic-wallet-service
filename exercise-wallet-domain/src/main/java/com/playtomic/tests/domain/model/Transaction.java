package com.playtomic.tests.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder
@Value
@Data
public class Transaction {
    String id;
    String walletId;
    String tokenizedCardId;
    String externalId;
    CurrencyAmount amount;
    TransactionType type;
}
