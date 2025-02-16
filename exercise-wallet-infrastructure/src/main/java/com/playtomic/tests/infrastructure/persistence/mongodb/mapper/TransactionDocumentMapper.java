package com.playtomic.tests.infrastructure.persistence.mongodb.mapper;

import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.TransactionDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CommonSubDocumentMapper.class})
public interface TransactionDocumentMapper {

    TransactionDocumentMapper INSTANCE = Mappers.getMapper(TransactionDocumentMapper.class);

    Transaction toDomain(TransactionDocument transactionDocument);

    TransactionDocument toDocument(Transaction transaction);
}
