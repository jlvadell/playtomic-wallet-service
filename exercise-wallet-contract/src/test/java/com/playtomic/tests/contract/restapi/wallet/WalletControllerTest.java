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
import com.playtomic.tests.domain.model.Transaction;
import com.playtomic.tests.domain.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    FindWalletByIdQueryHandler findWalletByIdQueryHandler;

    @Mock
    CreateTransactionCommandHandler createTransactionCommandHandler;

    @Mock
    WalletControllerCmdQueryMapper cmdQueryMapper;

    @Mock
    WalletControllerDtoMapper dtoMapper;

    @InjectMocks
    WalletController walletController;

    private static final String USER_ID = "U1";

    @BeforeEach
    void setupSecurityContext() {
        // Mock the SecurityContextHolder to return a dummy userId ("U1")
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(USER_ID);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("addTransaction should return a TransactionDto")
    void addTransaction_shouldReturnTransactionDto() {
        // Given
        var walletId = "walletId";
        var transactionRequestDto = new TransactionRequestDto();
        var cmd = CreateTransactionCommand.builder().build();
        var result = Transaction.builder().build();
        var expectedDto = new TransactionDto();
        // When
        when(cmdQueryMapper.toCommand(USER_ID, walletId, transactionRequestDto)).thenReturn(cmd);
        when(createTransactionCommandHandler.handle(cmd)).thenReturn(result);
        when(dtoMapper.toDto(result)).thenReturn(expectedDto);

        var actual = walletController.addTransaction(walletId, transactionRequestDto);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isNotNull().isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("findWalletById should return a WalletDto")
    void findWalletById_shouldReturnWalletDto() {
        // Given
        var walletId = "walletId";
        var query = FindWalletByIdQuery.builder().build();
        var result = Wallet.builder().build();
        var expectedDto = new WalletDto();
        // When
        when(cmdQueryMapper.toQuery(USER_ID, walletId)).thenReturn(query);
        when(findWalletByIdQueryHandler.handle(query)).thenReturn(result);
        when(dtoMapper.toDto(result)).thenReturn(expectedDto);

        var actual = walletController.findWalletById(walletId);
        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isNotNull().isEqualTo(expectedDto);
    }

}