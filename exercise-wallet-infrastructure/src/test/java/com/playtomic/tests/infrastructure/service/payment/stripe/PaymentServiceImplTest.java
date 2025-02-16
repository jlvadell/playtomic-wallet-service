package com.playtomic.tests.infrastructure.service.payment.stripe;

import com.playtomic.tests.domain.exception.PaymentProcessingException;
import com.playtomic.tests.infrastructure.service.payment.stripe.exception.StripeServiceException;
import com.playtomic.tests.infrastructure.service.payment.stripe.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.playtomic.tests.domain.model.fixture.TransactionFixtures.confirmedCardTransaction;
import static com.playtomic.tests.domain.model.fixture.TransactionFixtures.pendingCardTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    StripeService stripeService;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    @DisplayName("chargeTransaction should throw PaymentProcessingException when stripeService returns exception")
    void chargeTransaction_shouldThrowPaymentProcessingException_whenStripeServiceReturnsException() {
        // Given
        var transaction = pendingCardTransaction();

        // When
        when(stripeService.charge(pendingCardTransaction().getTokenizedCardId(),
                pendingCardTransaction().getAmount().toBigDecimal())).thenThrow(new StripeServiceException());

        // Then
        assertThatThrownBy(() -> paymentService.chargeTransaction(transaction))
                .isInstanceOf(PaymentProcessingException.class)
                .hasMessage("Error charging transaction");
    }

    @Test
    @DisplayName("chargeTransaction should return transaction with externalId on success")
    void chargeTransaction_shouldReturnTransactionWithExternalId_onSuccess() {
        // Given
        var transaction = pendingCardTransaction();
        var expected = transaction.toBuilder().externalId("P1").build();

        // When
        when(stripeService.charge(transaction.getTokenizedCardId(),
                transaction.getAmount().toBigDecimal())).thenReturn(new Payment("P1"));

        // Then
        var result = paymentService.chargeTransaction(transaction);
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(expected);
    }

    @Test
    @DisplayName("refundTransaction should throw PaymentProcessingException when stripeService returns exception")
    void refundTransaction_shouldThrowPaymentProcessingException_whenStripeServiceReturnsException() {
        // Given
        var transaction = confirmedCardTransaction();
        // When
        doThrow(new StripeServiceException()).when(stripeService).refund(transaction.getExternalId());
        // Then
        assertThatThrownBy(() -> paymentService.refundTransaction(transaction))
                .isInstanceOf(PaymentProcessingException.class)
                .hasMessage("Error refunding transaction");
    }

    @Test
    @DisplayName("refundTransaction should return transaction on success")
    void refundTransaction_shouldReturnTransaction_onSuccess() {
        // Given
        var transaction = confirmedCardTransaction();
        // When-Then
        var result = paymentService.refundTransaction(transaction);
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(transaction);
    }

}