package com.playtomic.tests.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Data
@Value
@Builder(toBuilder = true)
public class CurrencyAmount {
    String currency;
    int value;
    int decimal;

    public CurrencyAmount add(CurrencyAmount currencyAmount) {
        checkCurrencyCompatibility(currencyAmount);
        return CurrencyAmount.builder()
                .currency(this.currency)
                .value(this.value + currencyAmount.value)
                .decimal(this.decimal)
                .build();
    }

    public CurrencyAmount subtract(CurrencyAmount currencyAmount) {
        checkCurrencyCompatibility(currencyAmount);
        return CurrencyAmount.builder()
                .currency(this.currency)
                .value(this.value - currencyAmount.value)
                .decimal(this.decimal)
                .build();
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(this.value).movePointLeft(this.decimal);
    }

    public boolean isNegative() {
        return this.value < 0;
    }

    public int getAbsoluteValue() {
        return Math.abs(this.value);
    }

    public void checkCurrencyCompatibility(CurrencyAmount currencyAmount) {
        if (!this.currency.equals(currencyAmount.currency)) {
            throw new IllegalArgumentException("Currencies do not match: " + this.currency + " vs " + currencyAmount.currency);
        }
        if (this.decimal != currencyAmount.decimal) {
            throw new IllegalArgumentException("Decimal precision does not match: " + this.decimal + " vs " + currencyAmount.decimal);
        }
    }

}
