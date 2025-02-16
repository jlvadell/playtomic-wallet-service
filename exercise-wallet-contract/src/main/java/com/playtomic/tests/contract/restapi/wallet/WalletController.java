package com.playtomic.tests.contract.restapi.wallet;


import com.playtomic.tests.application.command.transaction.cmd.CreateTransactionCommand;
import com.playtomic.tests.application.command.transaction.handler.CreateTransactionCommandHandler;
import com.playtomic.tests.application.query.wallet.handler.FindWalletByIdQueryHandler;
import com.playtomic.tests.application.query.wallet.query.FindWalletByIdQuery;
import com.playtomic.tests.contract.restapi.wallet.mapper.WalletControllerCmdQueryMapper;
import com.playtomic.tests.contract.restapi.wallet.mapper.WalletControllerDtoMapper;
import com.playtomic.tests.contract.restapi.wallet.model.TransactionDto;
import com.playtomic.tests.contract.restapi.wallet.model.TransactionRequestDto;
import com.playtomic.tests.contract.restapi.wallet.model.WalletDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WalletController implements WalletApi {

    private final FindWalletByIdQueryHandler findWalletByIdQueryHandler;

    private final CreateTransactionCommandHandler createTransactionCommandHandler;

    private final WalletControllerCmdQueryMapper cmdQueryMapper;

    private final WalletControllerDtoMapper dtoMapper;


    @Override
    public ResponseEntity<TransactionDto> addTransaction(String walletId, TransactionRequestDto transactionRequestDto) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CreateTransactionCommand cmd = cmdQueryMapper.toCommand(userId, walletId, transactionRequestDto);

        return new ResponseEntity<>(dtoMapper.toDto(createTransactionCommandHandler.handle(cmd)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<WalletDto> findWalletById(String walletId) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FindWalletByIdQuery query = cmdQueryMapper.toQuery(userId, walletId);
        return new ResponseEntity<>(dtoMapper.toDto(findWalletByIdQueryHandler.handle(query)), HttpStatus.OK);
    }
}
