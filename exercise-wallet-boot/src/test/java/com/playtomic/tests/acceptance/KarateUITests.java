package com.playtomic.tests.acceptance;

import com.intuit.karate.junit5.Karate;
import com.playtomic.tests.WalletApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class KarateUITests {

    static ConfigurableApplicationContext context;

    @BeforeAll
    static void startApplication() {
        context = SpringApplication.run(WalletApplication.class, "--spring.profiles.active=test");
    }

    @AfterAll
    static void stopApplication() {
        context.close();
    }

    @Karate.Test
    Karate testWalletApi() {
        return Karate.run("wallet").relativeTo(getClass());
    }
}
