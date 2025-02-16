package com.playtomic.tests.application.query.wallet.handler;

import com.playtomic.tests.application.query.wallet.query.FindWalletByIdQuery;
import com.playtomic.tests.domain.repository.WalletRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.playtomic.tests.domain.model.fixture.WalletFixtures.walletWithHundredEuros;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindWalletByIdQueryHandlerTest {

    @Mock
    Validator validator;

    @Mock
    WalletRepository walletRepository;

    @InjectMocks
    FindWalletByIdQueryHandler handler;

    @Test
    @DisplayName("handle should throw exception when query is invalid")
    void handle_shouldThrowException_whenQueryIsInvalid() {
        // Given
        var query = FindWalletByIdQuery.builder().build();
        var violation = mock(ConstraintViolation.class);

        // When-Then
        when(validator.validate(query)).thenReturn(Set.of(violation));
        assertThatThrownBy(() -> handler.handle(query))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("handle should throw exception when wallet is not found")
    void handle_shouldThrowException_whenWalletIsNotFound() {
        // Given
        var query = FindWalletByIdQuery.builder()
                .walletId("W1")
                .userId("U1")
                .build();

        // When
        when(validator.validate(query)).thenReturn(Set.of());
        when(walletRepository.findById("W1")).thenReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> handler.handle(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wallet not found");
    }

    @Test
    @DisplayName("handle should throw exception when wallet does not belong to user")
    void handle_shouldThrowException_whenWalletDoesNotBelongToUser() {
        // Given
        var query = FindWalletByIdQuery.builder()
                .walletId("W1")
                .userId("someOtherUser")
                .build();
        var wallet = walletWithHundredEuros();

        // When
        when(validator.validate(query)).thenReturn(Set.of());
        when(walletRepository.findById("W1")).thenReturn(Optional.of(wallet));
        // Then
        assertThatThrownBy(() -> handler.handle(query))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unauthorized access to wallet");
    }

    @Test
    @DisplayName("handle should return wallet on success")
    void handle_shouldReturnWallet_onSuccess() {
        // Given
        var query = FindWalletByIdQuery.builder()
                .walletId("W1")
                .userId("U1")
                .build();
        var wallet = walletWithHundredEuros();

        // When
        when(validator.validate(query)).thenReturn(Set.of());
        when(walletRepository.findById("W1")).thenReturn(Optional.of(wallet));
        var actual = handler.handle(query);

        // Then
        assertThat(actual).isNotNull()
                .isEqualTo(wallet);

    }

}