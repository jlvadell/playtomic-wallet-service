package com.playtomic.tests.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
@Data
public class CurrencyAmount {
    String currency;
    int value;
    int decimal;

    public CurrencyAmount add(CurrencyAmount currencyAmount) {
        validateSameCurrencyAndDecimal(currencyAmount);
        return CurrencyAmount.builder()
                .currency(this.currency)
                .value(this.value + currencyAmount.value)
                .decimal(this.decimal)
                .build();
    }

    public CurrencyAmount subtract(CurrencyAmount currencyAmount) {
        validateSameCurrencyAndDecimal(currencyAmount);
        return CurrencyAmount.builder()
                .currency(this.currency)
                .value(this.value - currencyAmount.value)
                .decimal(this.decimal)
                .build();
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(this.value).movePointLeft(this.decimal);
    }

    private void validateSameCurrencyAndDecimal(CurrencyAmount currencyAmount) {
        if (!this.currency.equals(currencyAmount.currency)) {
            throw new IllegalArgumentException("Currencies do not match: " + this.currency + " vs " + currencyAmount.currency);
        }
        if (this.decimal != currencyAmount.decimal) {
            throw new IllegalArgumentException("Decimal precision does not match: " + this.decimal + " vs " + currencyAmount.decimal);
        }
    }

}
