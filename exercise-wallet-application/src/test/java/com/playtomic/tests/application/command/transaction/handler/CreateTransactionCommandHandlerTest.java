package com.playtomic.tests.application.command.transaction.handler;


import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;
import com.playtomic.tests.application.command.transaction.mapper.TransactionCommandMapper;
import com.playtomic.tests.application.exception.*;
import com.playtomic.tests.domain.exception.PaymentProcessingException;
import com.playtomic.tests.domain.exception.UnprocessableTransactionException;
import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.TransactionStatus;
import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.domain.repository.WalletRepository;
import com.playtomic.tests.domain.service.PaymentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.playtomic.tests.application.command.fixtures.CreateTransactionCommandFixtures.*;
import static com.playtomic.tests.domain.model.fixture.CurrencyAmountFixtures.negativeFiftyEuros;
import static com.playtomic.tests.domain.model.fixture.TransactionFixtures.*;
import static com.playtomic.tests.domain.model.fixture.WalletFixtures.walletWithFiftyEuros;
import static com.playtomic.tests.domain.model.fixture.WalletFixtures.walletWithHundredEuros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionCommandHandlerTest {

    @Mock
    Validator validator;

    @Mock
    WalletRepository walletRepository;

    @Mock
    PaymentService paymentService;

    @Mock
    TransactionCommandMapper mapper;

    @InjectMocks
    CreateTransactionCommandHandler handler;

    @Test
    @DisplayName("handle should throw exception when command has constraint violations")
    void handle_shouldThrowException_whenCommandHasConstraintViolations() {
        // Given
        var command = CreateTransactionCommand.builder().build();
        var violation = mock(ConstraintViolation.class);
        // When
        when(validator.validate(command)).thenReturn(Set.of(violation));
        // Then
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    @DisplayName("handle should throw exception when command has positive amount and no card")
    void handle_shouldThrowException_whenCommandHasPositiveAmountAndNoCard() {
        // Given
        var command = hundredEurosCardTransactionCommand().toBuilder()
                .tokenizedCardId(null)
                .build();
        // When
        when(validator.validate(command)).thenReturn(Set.of());
        // Then
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessage("Invalid transaction");

    }

    @Test
    @DisplayName("handle should throw exception when command has negative amount and a card")
    void handle_shouldThrowException_whenCommandHasNegativeAmountAndACard() {
        // Given
        var command = negativeFiftyEurosTransactionCommand().toBuilder()
                .tokenizedCardId("T1")
                .build();
        // When
        when(validator.validate(command)).thenReturn(Set.of());
        // Then
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessage("Invalid transaction");

    }

    @Test
    @DisplayName("handle should throw exception when wallet does not exist")
    void handle_shouldThrowException_whenWalletDoesNotExist() {
        // Given
        var command = hundredEurosCardTransactionCommand();
        // When
        when(validator.validate(command)).thenReturn(Set.of());
        when(walletRepository.findById(command.getWalletId())).thenReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessage("Wallet inaccessible");
    }

    @Test
    @DisplayName("handle should throw exception when wallet does not belong to user")
    void handle_shouldThrowException_whenWalletDoesNotBelongToUser() {
        // Given
        var command = hundredEurosCardTransactionCommand();
        var wallet = Wallet.builder()
                .id(command.getWalletId())
                .userId("anotherUser")
                .build();
        // When
        when(validator.validate(command)).thenReturn(Set.of());
        when(walletRepository.findById(command.getWalletId())).thenReturn(Optional.of(wallet));
        // Then
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(UnauthorizedUserException.class)
                .hasMessage("Wallet inaccessible");
    }

    @Test
    @DisplayName("handle should throw exception when wallet does not have enough balance")
    void handle_shouldThrowException_whenWalletDoesNotHaveEnoughBalance() {
        // Given
        var command = negativeHundredEurosTransactionCommand();
        var pendingTransaction = baseTransaction()
                .amount(CurrencyAmount.builder()
                        .value(-10000)
                        .decimal(2)
                        .currency("EUR")
                        .build())
                .status(TransactionStatus.PENDING)
                .build();
        var wallet = walletWithFiftyEuros();
        // When
        when(validator.validate(command)).thenReturn(Set.of());
        when(mapper.toDomain(command)).thenReturn(pendingTransaction);
        when(walletRepository.findById(command.getWalletId())).thenReturn(Optional.of(wallet));

        // Then
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient funds");
    }

    @Test
    @DisplayName("handle should rollback when repository fails to update balance")
    void handle_shouldRollback_whenRepositoryFailsToUpdateBalance() {
        // Given
        var command = negativeFiftyEurosTransactionCommand();
        var transaction = baseTransaction()
                .amount(negativeFiftyEuros())
                .status(TransactionStatus.PENDING)
                .build();
        var wallet = walletWithHundredEuros();
        var confirmedTransaction = transaction.toBuilder()
                .status(TransactionStatus.CONFIRMED)
                .build();
        // When
        when(validator.validate(command)).thenReturn(Set.of());
        when(walletRepository.findById(command.getWalletId())).thenReturn(Optional.of(wallet));
        when(mapper.toDomain(command)).thenReturn(transaction);
        when(walletRepository.updateBalance(any(Transaction.class))).thenThrow(UnprocessableTransactionException.class);

        // Then
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(PaymentTransactionException.class)
                .hasMessage("Error saving transaction");
        verify(paymentService).refundTransaction(confirmedTransaction);
    }

    @Test
    @DisplayName("handle should throw exception when rollback fails")
    void handle_shouldThrowException_whenRollbackFails() {
        // Given
        var command = negativeFiftyEurosTransactionCommand();
        var transaction = baseTransaction()
                .amount(negativeFiftyEuros())
                .status(TransactionStatus.PENDING)
                .build();
        var wallet = walletWithHundredEuros();
        var confirmedTransaction = transaction.toBuilder()
                .status(TransactionStatus.CONFIRMED)
                .build();
        // When
        when(validator.validate(command)).thenReturn(Set.of());
        when(walletRepository.findById(command.getWalletId())).thenReturn(Optional.of(wallet));
        when(mapper.toDomain(command)).thenReturn(transaction);
        when(walletRepository.updateBalance(any(Transaction.class))).thenThrow(UnprocessableTransactionException.class);
        when(paymentService.refundTransaction(confirmedTransaction)).thenThrow(PaymentProcessingException.class);

        // Then
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(InternalUnexpectedException.class)
                .hasMessage("System couldn't process your payment");
    }

    @Test
    @DisplayName("handle should return created transaction when spending from wallet is successful")
    void handle_shouldReturnTransaction_whenSpendingFromWalletIsSuccessful() {
        // Given
        var command = negativeFiftyEurosTransactionCommand();
        var wallet = walletWithHundredEuros();
        var pendingTransaction = baseTransaction()
                .amount(negativeFiftyEuros())
                .status(TransactionStatus.PENDING)
                .build();
        var confirmedTransaction = pendingTransaction.toBuilder()
                .status(TransactionStatus.CONFIRMED)
                .build();

        // When
        when(validator.validate(command)).thenReturn(Set.of());
        when(walletRepository.findById(command.getWalletId())).thenReturn(Optional.of(wallet));
        when(mapper.toDomain(command)).thenReturn(pendingTransaction);
        when(walletRepository.updateBalance(confirmedTransaction))
                .thenReturn(confirmedTransaction);

        var actual = handler.handle(command);

        // Then
        assertThat(actual).isEqualTo(confirmedTransaction);
        verifyNoInteractions(paymentService);
    }

    @Test
    @DisplayName("handle should return created transaction when topping-up wallet is successful")
    void handle_shouldReturnTransaction_whenToppingUpWalletIsSuccessful() {
        // Given
        var command = hundredEurosCardTransactionCommand();
        var wallet = walletWithHundredEuros();
        var pendingTransaction = pendingCardTransaction();
        var processedTransaction = processedPendingCardTransaction();
        var confirmedTransaction = confirmedCardTransaction();

        // When
        when(validator.validate(command)).thenReturn(Set.of());
        when(walletRepository.findById(command.getWalletId())).thenReturn(Optional.of(wallet));
        when(mapper.toDomain(command)).thenReturn(pendingTransaction);
        when(paymentService.chargeTransaction(pendingTransaction))
                .thenReturn(processedTransaction);
        when(walletRepository.updateBalance(confirmedTransaction))
                .thenReturn(confirmedTransaction);

        var actual = handler.handle(command);

        // Then
        assertThat(actual).isEqualTo(confirmedTransaction);
    }

}