package com.playtomic.tests.contract.restapi;


import com.playtomic.tests.contract.restapi.wallet.WalletApi;
import com.playtomic.tests.contract.restapi.wallet.model.TransactionDto;
import com.playtomic.tests.contract.restapi.wallet.model.TransactionRequestDto;
import com.playtomic.tests.contract.restapi.wallet.model.WalletDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController implements WalletApi {

    @Override
    public ResponseEntity<TransactionDto> addTransaction(String walletId, TransactionRequestDto transactionRequestDto) {
        return WalletApi.super.addTransaction(walletId, transactionRequestDto);
    }

    @Override
    public ResponseEntity<WalletDto> findWalletById(String walletId) {
        return WalletApi.super.findWalletById(walletId);
    }
}
