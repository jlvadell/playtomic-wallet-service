package com.playtomic.tests.infrastructure.persistence.mongodb;

import com.playtomic.tests.domain.exception.UnprocessableTransactionException;
import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.infrastructure.persistence.mongodb.mapper.TransactionDocumentMapper;
import com.playtomic.tests.infrastructure.persistence.mongodb.mapper.WalletDocumentMapper;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.CurrencyAmountSubDocument;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.TransactionDocument;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.WalletDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MongoDbTestConfig.class, WalletRepositoryImpl.class})
public class WalletRepositoryIT {

    @Autowired
    private WalletDocumentMapper walletDocumentMapper;

    @Autowired
    private TransactionDocumentMapper transactionDocumentMapper;

    @Autowired
    private WalletDao walletDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private WalletRepositoryImpl walletRepository;

    @BeforeEach
    void setUp() {
        walletDao.deleteAll(); // Clear test database
        transactionDao.deleteAll();
    }

    @Test
    @DisplayName("walletRepository should find wallet by id in MongoDB")
    void walletRepository_shouldFindWalletByIdInMongo() {
        // Given
        Wallet wallet = Wallet.builder()
                .id("wallet123")
                .userId("user123")
                .balance(CurrencyAmount.builder()
                        .value(1000)
                        .decimal(2)
                        .currency("EUR")
                        .build())

                .build();
        walletDao.save(new WalletDocument(wallet.getId(), wallet.getUserId(),
                new CurrencyAmountSubDocument(wallet.getBalance().getValue(),
                        wallet.getBalance().getDecimal(),
                        wallet.getBalance().getCurrency())));

        // When
        Optional<Wallet> foundWallet = walletRepository.findById("wallet123");

        // Then
        assertThat(foundWallet).isPresent();
        assertThat(foundWallet.get().getUserId()).isEqualTo("user123");
        assertThat(foundWallet.get().getBalance().getValue()).isEqualTo(1000);
    }

    @Test
    @DisplayName("walletRepository should perform transaction to update balance and save transaction in MongoDB")
    void walletRepository_shouldPerformTransactionToUpdateBalanceAndSaveTransactionInMongo() {
        // Given
        Wallet wallet = Wallet.builder()
                .id("wallet123")
                .userId("user123")
                .balance(CurrencyAmount.builder()
                        .value(1000)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();
        walletDao.save(new WalletDocument(wallet.getId(), wallet.getUserId(),
                new CurrencyAmountSubDocument(wallet.getBalance().getValue(),
                        wallet.getBalance().getDecimal(),
                        wallet.getBalance().getCurrency())));

        Transaction transaction = Transaction.builder()
                .walletId("wallet123")
                .amount(CurrencyAmount.builder()
                        .value(500)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();

        // When
        var savedTransaction = walletRepository.updateBalance(transaction);

        // Then
        Optional<Wallet> updatedWallet = walletRepository.findById("wallet123");
        assertThat(updatedWallet).isPresent();
        assertThat(updatedWallet.get().getBalance().getValue()).isEqualTo(1500);

        Optional<TransactionDocument> storedTransaction = transactionDao.findById(savedTransaction.getId());
        assertThat(storedTransaction).isPresent();
        assertThat(storedTransaction.get().getAmount().getValue()).isEqualTo(500);
    }

    @Test
    @DisplayName("walletRepository should rollback transaction when wallet can't apply transaction")
    void walletRepository_shouldRollbackTransaction_whenWalletCannotApplyTransaction() {
        // Given
        Wallet wallet = Wallet.builder()
                .id("wallet123")
                .userId("user123")
                .balance(CurrencyAmount.builder()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();
        walletDao.save(new WalletDocument(wallet.getId(), wallet.getUserId(),
                new CurrencyAmountSubDocument(wallet.getBalance().getValue(),
                        wallet.getBalance().getDecimal(),
                        wallet.getBalance().getCurrency())));
        Transaction transaction = Transaction.builder()
                .walletId("wallet123")
                .amount(CurrencyAmount.builder()
                        .value(-500)
                        .decimal(2)
                        .currency("EUR")
                        .build()
                ).build();

        // When
        assertThatThrownBy(() -> walletRepository.updateBalance(transaction))
                .isInstanceOf(UnprocessableTransactionException.class);

        // Then
        Optional<WalletDocument> storedWallet = walletDao.findById("wallet123");
        assertThat(storedWallet).isPresent();
        assertThat(storedWallet.get().getBalance().getValue()).isEqualTo(100);
    }
}
