package com.playtomic.tests.service;

import com.playtomic.tests.exception.*;
import com.playtomic.tests.model.CurrencyAmount;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.TransactionStatus;
import com.playtomic.tests.model.Wallet;
import com.playtomic.tests.repository.WalletRepository;
import com.playtomic.tests.service.stripe.StripeService;
import com.playtomic.tests.service.stripe.exception.StripeServiceException;
import com.playtomic.tests.service.stripe.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.playtomic.tests.model.fixture.CurrencyAmountFixtures.fiftyEuros;
import static com.playtomic.tests.model.fixture.CurrencyAmountFixtures.hundredEuros;
import static com.playtomic.tests.model.fixture.TransactionFixtures.confirmedCardTransaction;
import static com.playtomic.tests.model.fixture.TransactionFixtures.pendingCardTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    WalletRepository walletRepository;

    @Mock
    StripeService stripeService;

    @InjectMocks
    WalletService walletService;

    @Test
    @DisplayName("findWalletById should return wallet when wallet exists and belongs to user")
    void findWalletById_shouldReturnWallet_whenWalletExistsAndBelongsToUser() {
        // Given
        Wallet wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));

        // When
        var result = walletService.findWalletById(wallet.getUserId(), wallet.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(wallet);
    }

    @Test
    @DisplayName("findWalletById should throw exception when wallet does not exist")
    void findWalletById_shouldThrowException_whenWalletDoesNotExist() {
        // Given
        Wallet wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.empty());

        // When-Then
        assertThatThrownBy(() -> walletService.findWalletById(wallet.getUserId(), wallet.getId()))
                .isExactlyInstanceOf(UnprocessableEntityException.class)
                .hasMessage("Wallet not found");
    }

    @Test
    @DisplayName("findWalletById should throw exception when wallet does not belong to user")
    void findWalletById_shouldThrowException_whenWalletDoesNotBelongToUser() {
        // Given
        Wallet wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));

        // When-Then
        assertThatThrownBy(() -> walletService.findWalletById("U2", wallet.getId()))
                .isExactlyInstanceOf(UnauthorizedUserException.class)
                .hasMessage("Wallet inaccessible");
    }

    @Test
    @DisplayName("createTransaction should process and save card payment transaction")
    void createTransaction_shouldProcessAndSaveCardPaymentTransaction() {
        // Given
        var transaction = pendingCardTransaction();
        var confirmedTransaction = confirmedCardTransaction();
        var wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(CurrencyAmount.builder()
                        .value(10000)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(stripeService.charge(transaction.getTokenizedCardId(), transaction.getAmount().toBigDecimal()))
                .thenReturn(new Payment("E1"));
        when(walletRepository.updateBalance(confirmedTransaction)).thenReturn(confirmedTransaction);

        // When
        var result = walletService.createTransaction(transaction);

        // Then
        assertThat(result).isNotNull().isEqualTo(confirmedTransaction);
    }

    @Test
    @DisplayName("createTransaction should process and save balance transaction")
    void createTransaction_shouldProcessAndSaveBalanceTransaction() {
        // Given
        var transaction = Transaction.builder()
                .id("T1")
                .walletId("W1")
                .userId("U1")
                .amount(hundredEuros().negate())
                .status(TransactionStatus.PENDING)
                .build();
        var confirmedTransaction = transaction.toBuilder().status(TransactionStatus.CONFIRMED).build();
        var wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(hundredEuros())
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(walletRepository.updateBalance(confirmedTransaction)).thenReturn(confirmedTransaction);

        // When
        var result = walletService.createTransaction(transaction);

        // Then
        assertThat(result).isNotNull().isEqualTo(confirmedTransaction);
    }

    @Test
    @DisplayName("createTransaction should throw exception when there's no balance")
    void createTransaction_shouldThrowException_whenThereIsNoBalance() {
        // Given
        var transaction = Transaction.builder()
                .id("T1")
                .walletId("W1")
                .userId("U1")
                .amount(hundredEuros().negate())
                .status(TransactionStatus.PENDING)
                .build();
        var wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(fiftyEuros())
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));

        // When-Then
        assertThatThrownBy(() -> walletService.createTransaction(transaction))
                .isExactlyInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient funds");
    }

    @Test
    @DisplayName("createTransaction should throw exception when card payment fails")
    void createTransaction_shouldThrowException_whenCardPaymentFails() {
        // Given
        var transaction = pendingCardTransaction();
        var wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(CurrencyAmount.builder()
                        .value(10000)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(stripeService.charge(transaction.getTokenizedCardId(), transaction.getAmount().toBigDecimal()))
                .thenThrow(new StripeServiceException());

        // When-Then
        assertThatThrownBy(() -> walletService.createTransaction(transaction))
                .isExactlyInstanceOf(UnprocessableTransactionException.class)
                .hasMessage("Error charging transaction");
    }

    @Test
    @DisplayName("createTransaction should rollback when transaction fails")
    void createTransaction_shouldRollback_whenTransactionFails() {
        // Given
        var transaction = pendingCardTransaction();
        var confirmedTransaction = confirmedCardTransaction();
        var wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(CurrencyAmount.builder()
                        .value(10000)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(stripeService.charge(transaction.getTokenizedCardId(), transaction.getAmount().toBigDecimal()))
                .thenReturn(new Payment("E1"));
        when(walletRepository.updateBalance(confirmedTransaction))
                .thenThrow(new UnprocessableTransactionException("Error saving transaction", "Error saving transaction"));

        // When-Then
        assertThatThrownBy(() -> walletService.createTransaction(transaction))
                .isExactlyInstanceOf(UnprocessableTransactionException.class)
                .hasMessage("Error saving transaction");
        verify(stripeService).refund(confirmedTransaction.getExternalId());
    }

    @Test
    @DisplayName("createTransaction should throw exception when rollback fails")
    void createTransaction_shouldThrowException_whenRollbackFails() {
        // Given
        var transaction = pendingCardTransaction();
        var confirmedTransaction = confirmedCardTransaction();
        var wallet = Wallet.builder()
                .id("W1")
                .userId("U1")
                .balance(CurrencyAmount.builder()
                        .value(10000)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .build();
        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(stripeService.charge(transaction.getTokenizedCardId(), transaction.getAmount().toBigDecimal()))
                .thenReturn(new Payment("E1"));
        when(walletRepository.updateBalance(confirmedTransaction))
                .thenThrow(new UnprocessableTransactionException("Error saving transaction", "Error saving transaction"));
        doThrow(new StripeServiceException()).when(stripeService).refund(confirmedTransaction.getExternalId());

        // When-Then
        assertThatThrownBy(() -> walletService.createTransaction(transaction))
                .isExactlyInstanceOf(InternalUnexpectedException.class)
                .hasMessage("Payment Processing error");
        verify(stripeService).refund(confirmedTransaction.getExternalId());
    }

}