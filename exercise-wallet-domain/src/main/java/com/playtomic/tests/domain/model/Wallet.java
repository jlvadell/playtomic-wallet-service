package com.playtomic.tests.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder
@Value
@Data
public class Wallet {
    String id;
    String userId;
    CurrencyAmount balance;


}
