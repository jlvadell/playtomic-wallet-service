package com.playtomic.tests.infrastructure.persistence.mongodb.mapper;

import com.playtomic.tests.domain.model.CurrencyAmount;
import com.playtomic.tests.infrastructure.persistence.mongodb.model.CurrencyAmountSubDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommonSubDocumentMapper {

    CommonSubDocumentMapper INSTANCE = Mappers.getMapper(CommonSubDocumentMapper.class);

    CurrencyAmount toDomain(CurrencyAmountSubDocument currencyAmountDocument);

    CurrencyAmountSubDocument toDocument(CurrencyAmount currencyAmount);
}
