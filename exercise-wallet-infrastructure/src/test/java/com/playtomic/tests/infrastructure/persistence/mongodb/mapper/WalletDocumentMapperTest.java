package com.playtomic.tests.infrastructure.persistence.mongodb.mapper;

import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.CurrencyAmountSubDocument;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.WalletDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletDocumentMapperTest {

    @Mock
    CommonSubDocumentMapper commonSubDocumentMapper;

    @InjectMocks
    WalletDocumentMapperImpl walletDocumentMapper;

    @Test
    @DisplayName("toDomain should map a WalletDocument to a Wallet")
    void toDomain_shouldMapWalletDocumentToWallet() {
        // Given
        var document = new WalletDocument("id", "userId",
                new CurrencyAmountSubDocument(100, 0, "EUR"));
        var expected = Wallet.builder()
                .id("id")
                .userId("userId")
                .balance(CurrencyAmount.builder()
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
        var actual = walletDocumentMapper.toDomain(document);
        // Then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("toDocument should map a Wallet to a WalletDocument")
    void toDocument_shouldMapWalletToWalletDocument() {
        // Given
        var wallet = Wallet.builder()
                .id("id")
                .userId("userId")
                .balance(CurrencyAmount.builder()
                        .value(100)
                        .decimal(0)
                        .currency("EUR")
                        .build())
                .build();
        var expected = new WalletDocument("id", "userId",
                new CurrencyAmountSubDocument(100, 0, "EUR"));
        // When
        when(commonSubDocumentMapper.toDocument(any())).thenReturn(new CurrencyAmountSubDocument(100, 0, "EUR"));
        var actual = walletDocumentMapper.toDocument(wallet);
        // Then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}