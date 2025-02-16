package com.playtomic.tests.infrastructure.persistence.mongodb;

import com.playtomic.tests.infrastructure.persistence.mongodb.model.TransactionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionDao extends MongoRepository<TransactionDocument, String> {
}
