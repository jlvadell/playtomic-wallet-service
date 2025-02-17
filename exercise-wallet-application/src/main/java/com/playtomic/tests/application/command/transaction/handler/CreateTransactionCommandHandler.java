package com.playtomic.tests.application.command.transaction.handler;

import com.playtomic.tests.application.command.CommandHandler;
import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;
import com.playtomic.tests.application.command.transaction.mapper.TransactionCommandMapper;
import com.playtomic.tests.application.exception.*;
import com.playtomic.tests.domain.exception.PaymentProcessingException;
import com.playtomic.tests.domain.exception.UnprocessableTransactionException;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.domain.repository.WalletRepository;
import com.playtomic.tests.domain.service.PaymentService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTransactionCommandHandler implements CommandHandler<CreateTransactionCommand, Transaction> {

    private final Validator validator;

    private final WalletRepository walletRepository;

    private final PaymentService paymentService;

    private final TransactionCommandMapper transactionCommandMapper;

    @Override
    public Transaction handle(CreateTransactionCommand command) {
        log.info("[CreateTransactionCommandHandler::handle] command: {}", command);
        validate(command);

        Wallet wallet = fetchAndVerifyOwnership(command);

        Transaction transaction = transactionCommandMapper.toDomain(command);

        if (transaction.requirePaymentProcessing()) {
            transaction = processCardPayment(transaction);
        } else if (transaction.requireBalanceCheck()) {
            transaction = authorizeBalanceTransaction(wallet, transaction);
        }

        return saveTransaction(transaction);
    }

    private void validate(CreateTransactionCommand command) {
        log.trace("[CreateTransactionCommandHandler::validate] command: {}", command);
        Set<ConstraintViolation<CreateTransactionCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            log.trace("[CreateTransactionCommandHandler::validate] command has constraint violations: {}", violations);
            throw new ConstraintViolationException(violations);
        }

        if (command.getCurrencyValue() >= 0 && command.getTokenizedCardId() == null) {
            log.trace("[CreateTransactionCommandHandler::validate] Positive amount require card details, cmd: {}", command);
            throw new UnprocessableEntityException("Invalid transaction", "Positive amount require card details");
        }

        if (command.getCurrencyValue() < 0 && command.getTokenizedCardId() != null) {
            log.trace("[CreateTransactionCommandHandler::validate] Card transaction with negative amount, cmd: {}", command);
            throw new UnprocessableEntityException("Invalid transaction", "Negative amount not allowed for card transaction");
        }
    }

    private Wallet fetchAndVerifyOwnership(CreateTransactionCommand command) {
        log.trace("[CreateTransactionCommandHandler::fetchAndVerifyOwnership] command: {}", command);
        Wallet wallet = walletRepository.findById(command.getWalletId())
                .orElseThrow(() -> new UnprocessableEntityException("Wallet inaccessible",
                        "Wallet was not found"));

        if (!command.getUserId().equals(wallet.getUserId())) {
            throw new UnauthorizedUserException("Wallet inaccessible", "Wallet does not belong to user");
        }

        return wallet;
    }

    private Transaction processCardPayment(Transaction transaction) {
        log.trace("[CreateTransactionCommandHandler::processCardPayment] transaction: {}", transaction);
        try {
            Transaction chargedTransaction = paymentService.chargeTransaction(transaction);
            return chargedTransaction.confirmCardPayment();
        } catch (PaymentProcessingException ex) {
            log.error("[CreateTransactionCommandHandler::processCardPayment] Error charging transaction: {}", transaction, ex);
            throw new PaymentTransactionException("Error charging transaction", ex.getDetails());
        }
    }

    private Transaction authorizeBalanceTransaction(Wallet wallet, Transaction transaction) {
        log.trace("[CreateTransactionCommandHandler::processWalletPayment] wallet: {}, transaction: {}", wallet, transaction);
        if (!wallet.hasSufficientFunds(transaction.getAmount())) {
            throw new InsufficientFundsException("Insufficient funds", "Not enough balance");
        }
        return transaction.confirm();
    }

    private Transaction saveTransaction(Transaction transaction) {
        log.trace("[CreateTransactionCommandHandler::saveTransaction] transaction: {}", transaction);
        try {
            return walletRepository.updateBalance(transaction);
        } catch (UnprocessableTransactionException ex) {
            log.error("[CreateTransactionCommandHandler::saveTransaction] Error saving transaction: {}", transaction, ex);
            rollbackTransaction(transaction);
            throw new PaymentTransactionException("Error saving transaction", ex.getDetails());
        }
    }

    private void rollbackTransaction(Transaction transaction) {
        log.trace("[CreateTransactionCommandHandler::rollbackTransaction] transaction: {}", transaction);
        try {
            paymentService.refundTransaction(transaction);
        } catch (PaymentProcessingException ex) {
            log.error("[CreateTransactionCommandHandler::rollbackTransaction] Error rolling back, cannot refund transaction: {}", transaction, ex);
            throw new InternalUnexpectedException("System couldn't process your payment", ex.getDetails());
        }
    }
}
