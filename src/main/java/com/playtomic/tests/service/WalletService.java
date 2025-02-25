package com.playtomic.tests.service;

import com.playtomic.tests.exception.UnauthorizedUserException;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.Wallet;
import com.playtomic.tests.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;


    public Transaction createTransaction(Transaction transaction) {
        return null;
    }

    public Wallet findWalletById(String userId, String walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found with id " + walletId));

        // Verify that the wallet belongs to the given user
        if (!wallet.getUserId().equals(userId)) {
            throw new UnauthorizedUserException("Wallet inaccessible", "Wallet " + walletId + " does not belong to user " + userId);
        }

        return wallet;
    }
}

