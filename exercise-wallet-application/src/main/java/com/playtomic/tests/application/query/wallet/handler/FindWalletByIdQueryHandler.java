package com.playtomic.tests.application.query.wallet.handler;

import com.playtomic.tests.application.query.QueryHandler;
import com.playtomic.tests.application.query.wallet.query.FindWalletByIdQuery;
import com.playtomic.tests.domain.model.Wallet;
import com.playtomic.tests.domain.repository.WalletRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindWalletByIdQueryHandler implements QueryHandler<FindWalletByIdQuery, Wallet> {

    private final Validator validator;
    private final WalletRepository walletRepository;


    @Override
    public Wallet handle(FindWalletByIdQuery query) {
        log.info("[FindWalletByIdQueryHandler::handle] query: {}", query);
        validate(query);

        return fetchAndVerifyOwnership(query);
    }

    private Wallet fetchAndVerifyOwnership(FindWalletByIdQuery query) {
        log.trace("[FindWalletByIdQueryHandler::fetchAndVerifyOwnership] query: {}", query);
        Wallet wallet = walletRepository.findById(query.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        if (!Objects.equals(wallet.getUserId(), query.getUserId())) {
            throw new IllegalArgumentException("Unauthorized access to wallet");
        }

        return wallet;
    }

    private void validate(FindWalletByIdQuery query) {
        log.trace("[FindWalletByIdQueryHandler::validate] query: {}", query);
        Set<ConstraintViolation<FindWalletByIdQuery>> violations = validator.validate(query);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
