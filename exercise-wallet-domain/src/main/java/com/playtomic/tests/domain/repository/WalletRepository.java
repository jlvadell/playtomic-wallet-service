package com.playtomic.tests.domain.repository;

import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.Wallet;

import java.util.List;
import java.util.Optional;

public interface WalletRepository {

    Optional<Wallet> findById(String id);

    List<Transaction> findTransactionsByWalletId(String walletId, int page, int size);

    Optional<Transaction> updateBalance(Transaction transaction);

    Optional<Transaction> updateTransaction(Transaction transaction);
}
