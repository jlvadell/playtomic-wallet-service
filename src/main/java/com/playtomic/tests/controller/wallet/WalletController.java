package com.playtomic.tests.controller.wallet;

import com.playtomic.tests.controller.wallet.model.TransactionDto;
import com.playtomic.tests.controller.wallet.model.TransactionRequestDto;
import com.playtomic.tests.controller.wallet.model.WalletDto;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WalletController implements WalletApi {

    private final WalletService service;

    private final WalletControllerDtoMapper dtoMapper;


    @Override
    public ResponseEntity<TransactionDto> addTransaction(String walletId, TransactionRequestDto transactionRequestDto) {
        log.info("[WalletController::addTransaction] walletId: {}, transactionRequestDto: {}", walletId, transactionRequestDto);
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = dtoMapper.toDomain(userId, walletId, transactionRequestDto);

        return new ResponseEntity<>(dtoMapper.toDto(service.createTransaction(transaction)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WalletDto> findWalletById(String walletId) {
        log.info("[WalletController::findWalletById] walletId: {}", walletId);
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(dtoMapper.toDto(service.findWalletById(userId, walletId)), HttpStatus.OK);
    }
}
