package com.playtomic.tests.controller.wallet;

import com.playtomic.tests.controller.wallet.model.CurrencyAmountDto;
import com.playtomic.tests.controller.wallet.model.TransactionDto;
import com.playtomic.tests.controller.wallet.model.WalletDto;
import com.playtomic.tests.model.CurrencyAmount;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WalletControllerDtoMapper {

    WalletControllerDtoMapper INSTANCE = Mappers.getMapper(WalletControllerDtoMapper.class);

    TransactionDto toDto(Transaction transaction);

    WalletDto toDto(Wallet wallet);

    Transaction toDomain(String userId, String walletId, TransactionDto transactionDto);

    CurrencyAmount toDomain(CurrencyAmountDto currencyAmountDto);

    CurrencyAmountDto toDto(CurrencyAmount currencyAmount);
}
