package com.playtomic.tests.application.command.transaction.handler;

import com.playtomic.tests.application.command.CommandHandler;
import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;
import com.playtomic.tests.application.command.transaction.mapper.TransactionCommandMapper;
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

import java.util.Optional;
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
            throw new ConstraintViolationException(violations);
        }

        if (command.getCurrencyValue() >=0 && command.getTokenizedCardId() == null) {
            throw new IllegalArgumentException("Invalid top-up transaction amount - Card required");
        }

        if (command.getCurrencyValue() < 0 && command.getTokenizedCardId() != null) {
            throw new IllegalArgumentException("Invalid card transaction amount - Negative amount not allowed");
        }
    }

    private Wallet fetchAndVerifyOwnership(CreateTransactionCommand command) {
        log.trace("[CreateTransactionCommandHandler::fetchAndVerifyOwnership] command: {}", command);
        Wallet wallet = walletRepository.findById(command.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        if (!command.getUserId().equals(wallet.getUserId())) {
            throw new IllegalArgumentException("User not authorized to perform transaction");
        }

        return wallet;
    }

    private Transaction processCardPayment(Transaction transaction) {
        log.trace("[CreateTransactionCommandHandler::processCardPayment] transaction: {}", transaction);
        Optional<Transaction> chargedTransaction = paymentService.chargeTransaction(transaction);
        if (chargedTransaction.isEmpty()) {
            throw new RuntimeException("Error charging transaction");
        }
        return chargedTransaction.get().confirmCardPayment();
    }

    private Transaction authorizeBalanceTransaction(Wallet wallet, Transaction transaction) {
        log.trace("[CreateTransactionCommandHandler::processWalletPayment] wallet: {}, transaction: {}", wallet, transaction);
        if (!wallet.hasSufficientFunds(transaction.getAmount())) {
            throw new RuntimeException("Insufficient funds");
        }
        return transaction.confirm();
    }

    private Transaction saveTransaction(Transaction transaction) {
        log.trace("[CreateTransactionCommandHandler::saveTransaction] transaction: {}", transaction);
        Optional<Transaction> savedTransaction = walletRepository.updateBalance(transaction);
        if (savedTransaction.isEmpty()) {
            throw new RuntimeException("Error saving transaction");
        }
        return savedTransaction.get();
    }
}
