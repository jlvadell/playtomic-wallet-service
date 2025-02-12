package com.playtomic.tests.contract.restapi;


import com.playtomic.tests.contract.restapi.wallet.WalletApi;
import com.playtomic.tests.contract.restapi.wallet.model.Transaction;
import com.playtomic.tests.contract.restapi.wallet.model.TransactionRequest;
import com.playtomic.tests.contract.restapi.wallet.model.Wallet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController implements WalletApi {

    @Override
    public ResponseEntity<Transaction> addTransaction(String walletId, TransactionRequest transactionRequest) {
        return WalletApi.super.addTransaction(walletId, transactionRequest);
    }

    @Override
    public ResponseEntity<Wallet> findWalletById(String walletId) {
        return WalletApi.super.findWalletById(walletId);
    }
}
