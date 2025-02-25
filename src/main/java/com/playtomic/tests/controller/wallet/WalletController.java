package com.playtomic.tests.controller.wallet;

import com.playtomic.tests.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class WalletController {
/*
    private final WalletService service;

    private final WalletControllerDtoMapper dtoMapper;


    @Override
    public ResponseEntity<TransactionDto> addTransaction(String walletId, TransactionRequestDto transactionRequestDto) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Transaction transaction = dtoMapper.toDomain(userId, walletId, transactionRequestDto);

        return new ResponseEntity<>(dtoMapper.toDto(service.createTransaction(cmd)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WalletDto> findWalletById(String walletId) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(dtoMapper.toDto(service.findWalletById(userId, walletId)), HttpStatus.OK);
    }*/
}
