package com.playtomic.tests.repository;

import com.playtomic.tests.exception.UnprocessableTransactionException;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.Wallet;
import com.playtomic.tests.repository.mapper.TransactionDocumentMapper;
import com.playtomic.tests.repository.mapper.WalletDocumentMapper;
import com.playtomic.tests.repository.model.TransactionDocument;
import com.playtomic.tests.repository.model.WalletDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WalletRepository {

    private final MongoTemplate mongoTemplate;

    private final WalletDocumentMapper walletDocumentMapper;

    private final TransactionDocumentMapper transactionDocumentMapper;

    public Optional<Wallet> findById(String id) {
        log.trace("[WalletRepository::findById] walletId: {}", id);
        return Optional.ofNullable(mongoTemplate.findById(id, WalletDocument.class))
                .map(walletDocumentMapper::toDomain);
    }


    @Transactional(rollbackFor = UnprocessableTransactionException.class)
    public Transaction updateBalance(Transaction transaction) throws UnprocessableTransactionException{
        log.trace("[WalletRepository::updateBalance] transaction: {}", transaction);
        Criteria criteria = Criteria.where("id").is(transaction.getWalletId());

        if (transaction.getAmount().isNegative()) {
            criteria.and("balance.amount").gte(transaction.getAmount().getAbsoluteValue());
        }

        Query query = new Query(criteria);
        Update update = new Update().inc("balance.amount", transaction.getAmount().getValue());
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);
        WalletDocument updatedWallet = mongoTemplate.findAndModify(query, update, options, WalletDocument.class);
        if (updatedWallet == null) {
            log.error("[WalletRepository::updateBalance] Insufficient funds: {}", transaction.getWalletId());
            throw new UnprocessableTransactionException("Insufficient funds", "Unable to process transaction, for wallet: "
                    + transaction.getWalletId());
        }
        TransactionDocument savedTransaction = mongoTemplate.save(transactionDocumentMapper.toDocument(transaction));

        return transaction.updateId(savedTransaction.getId());
    }

}

