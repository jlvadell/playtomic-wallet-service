package com.playtomic.tests.repository.mapper;

import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.repository.model.TransactionDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CommonSubDocumentMapper.class})
public interface TransactionDocumentMapper {

    TransactionDocumentMapper INSTANCE = Mappers.getMapper(TransactionDocumentMapper.class);

    Transaction toDomain(TransactionDocument transactionDocument);

    TransactionDocument toDocument(Transaction transaction);
}
