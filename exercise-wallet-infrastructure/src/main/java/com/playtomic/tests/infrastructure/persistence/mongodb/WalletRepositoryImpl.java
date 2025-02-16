package com.playtomic.tests.infrastructure.persistence.mongodb;

import com.playtomic.tests.domain.exception.UnprocessableTransactionException;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.domain.repository.WalletRepository;
import com.playtomic.tests.infrastructure.persistence.mongodb.exception.TransactionErrorException;
import com.playtomic.tests.infrastructure.persistence.mongodb.mapper.TransactionDocumentMapper;
import com.playtomic.tests.infrastructure.persistence.mongodb.mapper.WalletDocumentMapper;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.TransactionDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletDao walletDao;

    private final TransactionDao transactionDao;

    private final WalletDocumentMapper walletDocumentMapper;

    private final TransactionDocumentMapper transactionDocumentMapper;

    @Override
    public Optional<Wallet> findById(String id) {
        log.trace("[WalletRepositoryImpl::findById] walletId: {}", id);
        return walletDao.findById(id)
                .map(walletDocumentMapper::toDomain);
    }

    @Override
    public List<Transaction> findTransactionsByWalletId(String walletId, int page, int size) {
        log.trace("[WalletRepositoryImpl::findTransactionsByWalletId] walletId: {}", walletId);
        //TODO: filter by walletId (new dao method - findByWalletId)
        Page<TransactionDocument> transactionPage = transactionDao.findAll(PageRequest.of(page, size));
        return transactionPage.get()
                .map(transactionDocumentMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = UnprocessableTransactionException.class)
    public Transaction updateBalance(Transaction transaction) {
        log.trace("[WalletRepositoryImpl::updateBalance] transaction: {}", transaction);
        Wallet wallet = walletDao.findById(transaction.getWalletId())
                .map(walletDocumentMapper::toDomain)
                .orElseThrow(() -> {
                    log.error("[WalletRepositoryImpl::updateBalance] Wallet not found: {}", transaction.getWalletId());
                    return new TransactionErrorException("Wallet not found");
                });

        wallet = wallet.applyTransaction(transaction);
        walletDao.save(walletDocumentMapper.toDocument(wallet));
        transactionDao.save(transactionDocumentMapper.toDocument(transaction));

        return transaction;
    }

}
