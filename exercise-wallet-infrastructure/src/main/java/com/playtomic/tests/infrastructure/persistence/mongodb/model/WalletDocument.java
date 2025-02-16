package com.playtomic.tests.infrastructure.persistence.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("wallets")
public class WalletDocument {
    @Id
    private String id;
    private String userId;
    private CurrencyAmountSubDocument balance;
}
