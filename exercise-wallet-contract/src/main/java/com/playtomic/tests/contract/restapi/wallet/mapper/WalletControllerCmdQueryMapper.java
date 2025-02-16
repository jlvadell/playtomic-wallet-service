package com.playtomic.tests.contract.restapi.wallet.mapper;

import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;
import com.playtomic.tests.application.query.wallet.query.FindWalletByIdQuery;
import com.playtomic.tests.contract.restapi.wallet.model.TransactionRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletControllerCmdQueryMapper {

    WalletControllerCmdQueryMapper INSTANCE = Mappers.getMapper(WalletControllerCmdQueryMapper.class);

    @Mapping(target="tokenizedCardId", source="transactionRequestDto.card")
    @Mapping(target="currencyValue", source="transactionRequestDto.amount.value")
    @Mapping(target="currencyDecimal", source="transactionRequestDto.amount.decimal")
    @Mapping(target="currency", source="transactionRequestDto.amount.currency")
    CreateTransactionCommand toCommand(String userId, String walletId, TransactionRequestDto transactionRequestDto);

    FindWalletByIdQuery toQuery(String userId, String walletId);

}
