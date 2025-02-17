package com.playtomic.tests.infrastructure.persistence.mongodb;

import com.playtomic.tests.infrastructure.persistence.mongodb.model.WalletDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletDao extends MongoRepository<WalletDocument, String> {

}
