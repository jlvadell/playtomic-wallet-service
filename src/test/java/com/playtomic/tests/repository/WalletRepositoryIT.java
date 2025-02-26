package com.playtomic.tests.repository;

import com.playtomic.tests.exception.UnprocessableTransactionException;
import com.playtomic.tests.model.CurrencyAmount;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.Wallet;
import com.playtomic.tests.repository.mapper.CommonSubDocumentMapperImpl;
import com.playtomic.tests.repository.mapper.TransactionDocumentMapperImpl;
import com.playtomic.tests.repository.mapper.WalletDocumentMapperImpl;
import com.playtomic.tests.repository.model.CurrencyAmountSubDocument;
import com.playtomic.tests.repository.model.TransactionDocument;
import com.playtomic.tests.repository.model.WalletDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest(properties = "de.flapdoodle.mongodb.embedded.version=7.0.0")
@Import({WalletRepository.class, WalletDocumentMapperImpl.class, TransactionDocumentMapperImpl.class, CommonSubDocumentMapperImpl.class})
class WalletRepositoryIT {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("wallets");
        mongoTemplate.dropCollection("transactions");
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
        mongoTemplate.save(new WalletDocument(wallet.getId(), wallet.getUserId(),
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
        mongoTemplate.save(new WalletDocument(wallet.getId(), wallet.getUserId(),
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

        TransactionDocument storedTransaction = mongoTemplate.findById(savedTransaction.getId(), TransactionDocument.class);
        assertThat(storedTransaction).isNotNull();
        assertThat(storedTransaction.getAmount().getValue()).isEqualTo(500);
    }

    @Test
    @DisplayName("walletRepository should handle negative transaction amount")
    void walletRepository_shouldHandleNegativeTransactionAmount() {
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
        mongoTemplate.save(new WalletDocument(wallet.getId(), wallet.getUserId(),
                new CurrencyAmountSubDocument(wallet.getBalance().getValue(),
                        wallet.getBalance().getDecimal(),
                        wallet.getBalance().getCurrency())));

        Transaction transaction = Transaction.builder()
                .walletId("wallet123")
                .amount(CurrencyAmount.builder()
                        .value(-500)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();

        // When
        var savedTransaction = walletRepository.updateBalance(transaction);

        // Then
        Optional<Wallet> updatedWallet = walletRepository.findById("wallet123");
        assertThat(updatedWallet).isPresent();
        assertThat(updatedWallet.get().getBalance().getValue()).isEqualTo(500);

        TransactionDocument storedTransaction = mongoTemplate.findById(savedTransaction.getId(), TransactionDocument.class);
        assertThat(storedTransaction).isNotNull();
        assertThat(storedTransaction.getAmount().getValue()).isEqualTo(-500);
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
        mongoTemplate.save(new WalletDocument(wallet.getId(), wallet.getUserId(),
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
        WalletDocument storedWallet = mongoTemplate.findById("wallet123", WalletDocument.class);
        assertThat(storedWallet).isNotNull();
        assertThat(storedWallet.getBalance().getValue()).isEqualTo(100);
    }
}
