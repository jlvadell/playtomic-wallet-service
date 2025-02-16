package com.playtomic.tests.infrastructure.persistence.mongodb.mapper;

import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.CurrencyAmountSubDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommonSubDocumentMapperTest {

    CommonSubDocumentMapper commonSubDocumentMapper = CommonSubDocumentMapper.INSTANCE;

    @Test
    @DisplayName("toDomain should map a CurrencyAmountSubDocument to a CurrencyAmount")
    void toDomain_shouldMapCurrencyAmountSubDocumentToCurrencyAmount() {
        // Given
        var currencyAmountSubDocument = new CurrencyAmountSubDocument(100, 0, "EUR");
        var expected = CurrencyAmount.builder()
                .value(100)
                .decimal(0)
                .currency("EUR")
                .build();
        // When
        var actual = commonSubDocumentMapper.toDomain(currencyAmountSubDocument);
        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("toDocument should map a CurrencyAmount to a CurrencyAmountSubDocument")
    void toDocument_shouldMapCurrencyAmountToCurrencyAmountSubDocument() {
        // Given
        var currencyAmount = CurrencyAmount.builder()
                .value(100)
                .decimal(0)
                .currency("EUR")
                .build();
        var expected = new CurrencyAmountSubDocument(100, 0, "EUR");
        // When
        var actual = commonSubDocumentMapper.toDocument(currencyAmount);
        // Then
        assertThat(actual).isEqualTo(expected);
    }

}