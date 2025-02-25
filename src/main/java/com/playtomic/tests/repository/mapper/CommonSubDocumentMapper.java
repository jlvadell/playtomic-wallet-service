package com.playtomic.tests.repository.mapper;

import com.playtomic.tests.model.CurrencyAmount;
import com.playtomic.tests.repository.model.CurrencyAmountSubDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommonSubDocumentMapper {

    CommonSubDocumentMapper INSTANCE = Mappers.getMapper(CommonSubDocumentMapper.class);

    CurrencyAmount toDomain(CurrencyAmountSubDocument currencyAmountDocument);

    CurrencyAmountSubDocument toDocument(CurrencyAmount currencyAmount);
}
