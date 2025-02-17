package com.playtomic.tests.infrastructure.persistence.mongodb.mapper;

import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.CurrencyAmountSubDocument;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.TransactionDocument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionDocumentMapperTest {

    @Mock
    CommonSubDocumentMapper commonSubDocumentMapper;

    @InjectMocks
    private TransactionDocumentMapperImpl transactionDocumentMapper;

    @Test
    @DisplayName("toDomain should map a TransactionDocument to a Transaction")
    void toDomain_shouldMapTransactionDocumentToTransaction() {
        // Given
        var document = new TransactionDocument("id", "walletId", new CurrencyAmountSubDocument(100, 0, "EUR"));
        var expected = Transaction.builder()
                .id("id")
                .walletId("walletId")
                .amount(CurrencyAmount.builder()
                        .value(100)
                        .decimal(0)
                        .currency("EUR")
                        .build())
                .build();
        // When
        when(commonSubDocumentMapper.toDomain(any())).thenReturn(CurrencyAmount.builder()
                .value(100)
                .decimal(0)
                .currency("EUR")
                .build());
        var actual = transactionDocumentMapper.toDomain(document);
        // Then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("toDocument should map a Transaction to a TransactionDocument")
    void toDocument_shouldMapTransactionToTransactionDocument() {
        // Given
        var transaction = Transaction.builder()
                .id("id")
                .walletId("walletId")
                .amount(CurrencyAmount.builder()
                        .value(100)
                        .decimal(0)
                        .currency("EUR")
                        .build())
                .build();
        var expected = new TransactionDocument("id", "walletId", new CurrencyAmountSubDocument(100, 0, "EUR"));
        // When
        when(commonSubDocumentMapper.toDocument(any())).thenReturn(new CurrencyAmountSubDocument(100, 0, "EUR"));
        var actual = transactionDocumentMapper.toDocument(transaction);
        // Then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}