package com.playtomic.tests.infrastructure.persistence.mongodb.mapper;

import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.WalletDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CommonSubDocumentMapper.class})
public interface WalletDocumentMapper {

    WalletDocumentMapper INSTANCE = Mappers.getMapper(WalletDocumentMapper.class);

    Wallet toDomain(WalletDocument walletDocument);

    WalletDocument toDocument(Wallet wallet);

}
