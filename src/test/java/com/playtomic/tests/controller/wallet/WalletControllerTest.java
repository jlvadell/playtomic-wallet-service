package com.playtomic.tests.controller.wallet;

import com.playtomic.tests.controller.wallet.model.CurrencyAmountDto;
import com.playtomic.tests.controller.wallet.model.TransactionDto;
import com.playtomic.tests.controller.wallet.model.TransactionRequestDto;
import com.playtomic.tests.controller.wallet.model.WalletDto;
import com.playtomic.tests.model.CurrencyAmount;
import com.playtomic.tests.model.Transaction;
import com.playtomic.tests.model.TransactionStatus;
import com.playtomic.tests.model.Wallet;
import com.playtomic.tests.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    WalletService service;

    @Mock
    WalletControllerDtoMapper dtoMapper;

    @InjectMocks
    WalletController controller;

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
    @DisplayName("addTransaction should return TransactionDto")
    void addTransaction_shouldReturnTransactionDto() {
        //Given
        var walletId = "walletId";
        var transactionRequestDto = new TransactionRequestDto()
                .amount(new CurrencyAmountDto()
                        .currency("EUR")
                        .decimal(2)
                        .value(100));
        var transaction = Transaction.builder().build();
        var transactionDto = new TransactionDto()
                .id("id")
                .status("CONFIRMED")
                .amount(new CurrencyAmountDto()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                );
        when(dtoMapper.toDomain(USER_ID, walletId, transactionRequestDto)).thenReturn(transaction);
        when(service.createTransaction(transaction)).thenReturn(transaction);
        when(dtoMapper.toDto(transaction)).thenReturn(transactionDto);
        //When
        var result = controller.addTransaction(walletId, transactionRequestDto);
        //Then
        assertThat(result).isNotNull().isEqualTo(ResponseEntity.ok(transactionDto));
    }

    @Test
    @DisplayName("findWalletById should return WalletDto")
    void findWalletById_shouldReturnWalletDto() {
        //Given
        var walletId = "walletId";
        var wallet = Wallet.builder().build();
        var walletDto = new WalletDto()
                .id("id")
                .balance(new CurrencyAmountDto()
                        .value(100)
                        .decimal(2)
                        .currency("EUR")
                );
        when(service.findWalletById(USER_ID, walletId)).thenReturn(wallet);
        when(dtoMapper.toDto(wallet)).thenReturn(walletDto);
        //When
        var result = controller.findWalletById(walletId);
        //Then
        assertThat(result).isNotNull().isEqualTo(ResponseEntity.ok(walletDto));
    }

}