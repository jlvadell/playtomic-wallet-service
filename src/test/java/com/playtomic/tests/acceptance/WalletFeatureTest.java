package com.playtomic.tests.acceptance;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.intuit.karate.junit5.Karate;
import com.playtomic.tests.repository.model.CurrencyAmountSubDocument;
import com.playtomic.tests.repository.model.WalletDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "spring.profiles.active=test",
                "de.flapdoodle.mongodb.embedded.version=7.0.0"
        }
)
@WireMockTest(httpPort = 9999)
public class WalletFeatureTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Karate.Test
    public Karate runWalletFeature() {
        WalletDocument walletDocument = WalletDocument.builder()
                .id("wallet1")
                .userId("U1")
                .balance(CurrencyAmountSubDocument.builder()
                        .currency("EUR")
                        .value(10000)
                        .decimal(2)
                        .build())
                .build();
        mongoTemplate.save(walletDocument);
        return Karate.run("src/test/resources/acceptance/wallet.feature");
    }
}