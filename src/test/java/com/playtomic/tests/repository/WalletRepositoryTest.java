package com.playtomic.tests.repository;

import com.playtomic.tests.exception.UnprocessableTransactionException;
import com.playtomic.tests.model.CurrencyAmount;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.Wallet;
import com.playtomic.tests.repository.mapper.TransactionDocumentMapper;
import com.playtomic.tests.repository.mapper.WalletDocumentMapper;
import com.playtomic.tests.repository.model.CurrencyAmountSubDocument;
import com.playtomic.tests.repository.model.TransactionDocument;
import com.playtomic.tests.repository.model.WalletDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.playtomic.tests.model.fixture.CurrencyAmountFixtures.hundredEuros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletRepositoryTest {

    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    WalletDocumentMapper walletDocumentMapper;

    @Mock
    TransactionDocumentMapper transactionDocumentMapper;

    @InjectMocks
    WalletRepository walletRepository;

    @Test
    @DisplayName("findById should return a Wallet")
    void findById_shouldReturnWallet() {
        // Given
        var walletId = "walletId";
        var walletDocument = new WalletDocument(walletId, "userId", new CurrencyAmountSubDocument(100, 0, "EUR"));
        var expected = Wallet.builder()
                .id(walletId)
                .userId("userId")
                .balance(CurrencyAmount.builder()
                        .value(100)
                        .decimal(0)
                        .currency("EUR")
                        .build())
                .build();
        // When
        when(mongoTemplate.findById(walletId, WalletDocument.class)).thenReturn(walletDocument);
        when(walletDocumentMapper.toDomain(walletDocument)).thenReturn(expected);
        var actual = walletRepository.findById(walletId);
        // Then
        assertThat(actual).contains(expected);
    }

    @Test
    @DisplayName("findById should return empty when wallet not found")
    void findById_shouldReturnEmptyWhenWalletNotFound() {
        // Given
        var walletId = "walletId";
        // When
        when(mongoTemplate.findById(walletId, WalletDocument.class)).thenReturn(null);
        var actual = walletRepository.findById(walletId);
        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("updateBalance should update the balance of a wallet, positive amount")
    void updateBalance_shouldUpdateBalanceOfWallet_whenAmountIsPositive() {
        // Given
        var walletId = "walletId";
        var transaction = Transaction.builder()
                .walletId(walletId)
                .amount(hundredEuros())
                .build();
        var walletDocument = new WalletDocument(walletId, "userId", new CurrencyAmountSubDocument(100, 0, "EUR"));
        var transactionDocToSave = new TransactionDocument();
        transactionDocToSave.setId("T1");
        // When
        when(mongoTemplate.findAndModify(any(), any(), any(), eq(WalletDocument.class))).thenReturn(walletDocument);
        when(transactionDocumentMapper.toDocument(transaction)).thenReturn(transactionDocToSave);
        when(mongoTemplate.save(transactionDocToSave)).thenReturn(transactionDocToSave);
        var actual = walletRepository.updateBalance(transaction);
        // Then
        assertThat(actual).isEqualTo(transaction.toBuilder().id("T1").build());
    }

    @Test
    @DisplayName("updateBalance should update the balance of a wallet, negative amount")
    void updateBalance_shouldUpdateBalanceOfWallet_whenAmountIsNegative() {
        // Given
        var walletId = "walletId";
        var transaction = Transaction.builder()
                .walletId(walletId)
                .amount(hundredEuros().negate())
                .build();
        var walletDocument = new WalletDocument(walletId, "userId", new CurrencyAmountSubDocument(100, 0, "EUR"));
        var transactionDocToSave = new TransactionDocument();
        transactionDocToSave.setId("T1");
        // When
        when(mongoTemplate.findAndModify(any(), any(), any(), eq(WalletDocument.class))).thenReturn(walletDocument);
        when(transactionDocumentMapper.toDocument(transaction)).thenReturn(transactionDocToSave);
        when(mongoTemplate.save(transactionDocToSave)).thenReturn(transactionDocToSave);
        var actual = walletRepository.updateBalance(transaction);
        // Then
        assertThat(actual).isEqualTo(transaction.toBuilder().id("T1").build());
    }

    @Test
    @DisplayName("updateBalance should throw an exception when the wallet is not found")
    void updateBalance_shouldThrowExceptionWhenWalletNotFound() {
        // Given
        var walletId = "walletId";
        var transaction = Transaction.builder()
                .walletId(walletId)
                .amount(hundredEuros().negate())
                .build();
        // When
        when(mongoTemplate.findAndModify(any(), any(), any(), eq(WalletDocument.class))).thenReturn(null);
        // Then
        assertThatThrownBy(() -> walletRepository.updateBalance(transaction))
                .isInstanceOf(UnprocessableTransactionException.class)
                .hasMessage("Insufficient funds");
    }


}