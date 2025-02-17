package com.playtomic.tests.application.command.transaction.mapper;

import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;
import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionCommandMapper {

    TransactionCommandMapper INSTANCE = Mappers.getMapper(TransactionCommandMapper.class);


    @Mapping(target = "amount", source = ".")
    @Mapping(target = "status", constant = "PENDING")
    Transaction toDomain(CreateTransactionCommand command);

    @Mapping(target = "value", source = "currencyValue")
    @Mapping(target = "decimal", source = "currencyDecimal")
    CurrencyAmount toDomainCurrencyAmount(CreateTransactionCommand command);


}
