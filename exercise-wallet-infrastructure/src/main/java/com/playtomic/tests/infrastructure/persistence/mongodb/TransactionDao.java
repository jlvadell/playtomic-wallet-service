package com.playtomic.tests.infrastructure.persistence.mongodb;

import com.playtomic.tests.infrastructure.persistence.mongodb.model.TransactionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDao extends MongoRepository<TransactionDocument, String> {
}
