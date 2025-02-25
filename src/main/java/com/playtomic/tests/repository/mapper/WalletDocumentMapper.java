package com.playtomic.tests.repository.mapper;

import com.playtomic.tests.model.Wallet;
import com.playtomic.tests.repository.model.WalletDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CommonSubDocumentMapper.class})
public interface WalletDocumentMapper {

    WalletDocumentMapper INSTANCE = Mappers.getMapper(WalletDocumentMapper.class);

    Wallet toDomain(WalletDocument walletDocument);

    WalletDocument toDocument(Wallet wallet);

}
