package com.playtomic.tests.infrastructure.persistence.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyAmountSubDocument {
    private int value;
    private int decimal;
    private String currency;
}
