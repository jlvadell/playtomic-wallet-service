package com.playtomic.tests.service;

import com.playtomic.tests.exception.*;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.Wallet;
import com.playtomic.tests.repository.WalletRepository;
import com.playtomic.tests.service.stripe.StripeService;
import com.playtomic.tests.service.stripe.exception.StripeServiceException;
import com.playtomic.tests.service.stripe.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final StripeService stripeService;


    public Transaction createTransaction(Transaction transaction) {
        log.info("[WalletService::createTransaction] Creating transaction {}", transaction);
        Wallet wallet = findWalletById(transaction.getUserId(), transaction.getWalletId());
        if (transaction.requirePaymentProcessing()) {
            transaction = processCardPayment(transaction);
        } else if (transaction.requireBalanceCheck()) {
            transaction = authorizeBalanceTransaction(wallet, transaction);
        }

        try {
            return walletRepository.updateBalance(transaction);
        } catch (UnprocessableTransactionException ex) {
            log.error("[WalletService::saveTransaction] Error saving transaction: {}", transaction, ex);
            if (transaction.requirePaymentProcessing()) {
                rollbackTransaction(transaction);
            }
            throw new UnprocessableTransactionException("Error saving transaction", ex.getDetails());
        }
    }

    private void rollbackTransaction(Transaction transaction) {
        log.trace("[WalletService::rollbackTransaction] transaction: {}", transaction);
        try {
            stripeService.refund(transaction.getExternalId());
        } catch (StripeServiceException ex) {
            log.error("[WalletService::rollbackTransaction] Error rolling back, cannot refund transaction: {}", transaction, ex);
            throw new InternalUnexpectedException("Payment Processing error", "System couldn't process your payment");
        }
    }

    private Transaction processCardPayment(Transaction transaction) {
        log.trace("[WalletService::processCardPayment] transaction: {}", transaction);
        try {
            //TODO: stripeService shold return Transaction instead of Payment
            Payment payment = stripeService.charge(transaction.getTokenizedCardId(), transaction.getAmount().toBigDecimal());
            transaction = transaction.updateExternalId(payment.getId());
            return transaction.confirmCardPayment();
        } catch (StripeServiceException ex) {
            log.error("[WalletService::processCardPayment] Error charging transaction: {}", transaction, ex);
            //TODO: handle specific stripe exceptions
            throw new UnprocessableTransactionException("Error charging transaction", "Amount too small");
        }
    }

    private Transaction authorizeBalanceTransaction(Wallet wallet, Transaction transaction) {
        log.trace("[WalletService::processWalletPayment] wallet: {}, transaction: {}", wallet, transaction);
        if (!wallet.hasSufficientFunds(transaction.getAmount())) {
            throw new InsufficientFundsException("Insufficient funds", "Not enough balance");
        }
        return transaction.confirm();
    }

    public Wallet findWalletById(String userId, String walletId) {
        log.info("[WalletService::findWalletById] Finding wallet with id {} for user {}", walletId, userId);
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> {
                    log.trace("[WalletService::findWalletById] Wallet not found with id {}", walletId);
                    return new UnprocessableEntityException("Wallet not found", "Wallet not found with id " + walletId);
                });

        // Verify that the wallet belongs to the given user
        if (!wallet.getUserId().equals(userId)) {
            log.trace("[WalletService::findWalletById] Wallet {} does not belong to user {}", walletId, userId);
            throw new UnauthorizedUserException("Wallet inaccessible", "Wallet " + walletId + " does not belong to user " + userId);
        }

        return wallet;
    }
}

