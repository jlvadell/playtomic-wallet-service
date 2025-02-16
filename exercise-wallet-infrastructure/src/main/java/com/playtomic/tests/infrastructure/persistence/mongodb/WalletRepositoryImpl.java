package com.playtomic.tests.infrastructure.persistence.mongodb;

import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.domain.repository.WalletRepository;

import java.util.List;
import java.util.Optional;

public class WalletRepositoryImpl implements WalletRepository {
    @Override
    public Optional<Wallet> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Transaction> findTransactionsByWalletId(String walletId, int page, int size) {
        return List.of();
    }

    @Override
    public Optional<Transaction> updateBalance(Transaction transaction) {
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> updateTransaction(Transaction transaction) {
        return Optional.empty();
    }
}
