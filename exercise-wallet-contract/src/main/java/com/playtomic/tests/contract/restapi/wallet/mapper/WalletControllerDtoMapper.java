package com.playtomic.tests.contract.restapi.wallet.mapper;

import com.playtomic.tests.contract.restapi.wallet.model.TransactionDto;
import com.playtomic.tests.contract.restapi.wallet.model.WalletDto;
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletControllerDtoMapper {

    WalletControllerDtoMapper INSTANCE = Mappers.getMapper(WalletControllerDtoMapper.class);

    TransactionDto toDto(Transaction transaction);

    WalletDto toDto(Wallet wallet);
}
