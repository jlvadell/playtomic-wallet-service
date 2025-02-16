package com.playtomic.tests.infrastructure.persistence.mongodb;

import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.infrastructure.persistence.mongodb.exception.TransactionErrorException;
import com.playtomic.tests.infrastructure.persistence.mongodb.mapper.TransactionDocumentMapper;
import com.playtomic.tests.infrastructure.persistence.mongodb.mapper.WalletDocumentMapper;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.CurrencyAmountSubDocument;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.TransactionDocument;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.WalletDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletRepositoryImplTest {

    @Mock
    WalletDao walletDao;

    @Mock
    WalletDocumentMapper walletDocumentMapper;

    @Mock
    TransactionDao transactionDao;

    @Mock
    TransactionDocumentMapper transactionDocumentMapper;

    @InjectMocks
    WalletRepositoryImpl walletRepository;

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
        when(walletDao.findById(walletId)).thenReturn(Optional.of(walletDocument));
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
        when(walletDao.findById(walletId)).thenReturn(Optional.empty());
        var actual = walletRepository.findById(walletId);
        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("updateBalance should update the balance of a wallet")
    void updateBalance_shouldUpdateBalanceOfWallet() {
        // Given
        var walletId = "walletId";
        var transaction = Transaction.builder()
                .walletId(walletId)
                .build();
        var walletDocument = new WalletDocument(walletId, "userId", new CurrencyAmountSubDocument(100, 0, "EUR"));
        var walletDocToSave = new WalletDocument();
        var transactionDocToSave = new TransactionDocument();
        var wallet = mock(Wallet.class);
        // When
        when(walletDao.findById(walletId)).thenReturn(Optional.of(walletDocument));
        when(walletDocumentMapper.toDomain(walletDocument)).thenReturn(wallet);
        when(wallet.applyTransaction(transaction)).thenReturn(wallet);
        when(walletDocumentMapper.toDocument(wallet)).thenReturn(walletDocToSave);
        when(transactionDocumentMapper.toDocument(transaction)).thenReturn(transactionDocToSave);
        var actual = walletRepository.updateBalance(transaction);
        // Then
        assertThat(actual).isEqualTo(transaction);
        verify(walletDao).save(walletDocToSave);
        verify(transactionDao).save(transactionDocToSave);
    }

    @Test
    @DisplayName("updateBalance should throw an exception when the wallet is not found")
    void updateBalance_shouldThrowExceptionWhenWalletNotFound() {
        // Given
        var walletId = "walletId";
        var transaction = Transaction.builder()
                .walletId(walletId)
                .build();
        // When
        when(walletDao.findById(walletId)).thenReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> walletRepository.updateBalance(transaction))
                .isInstanceOf(TransactionErrorException.class)
                .hasMessage("Wallet not found");
    }


}