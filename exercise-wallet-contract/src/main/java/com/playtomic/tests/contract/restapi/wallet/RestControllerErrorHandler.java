package com.playtomic.tests.contract.restapi.wallet;

import com.playtomic.tests.application.exception.*;
import com.playtomic.tests.contract.restapi.wallet.model.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerErrorHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorDto> handleInsufficientFundsException(InsufficientFundsException ex) {
        return new ResponseEntity<>(parseException(ex), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InternalUnexpectedException.class)
    public ResponseEntity<ErrorDto> handleInternalUnexpectedException(InternalUnexpectedException ex) {
        return new ResponseEntity<>(parseException(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PaymentTransactionException.class)
    public ResponseEntity<ErrorDto> handlePaymentTransactionException(PaymentTransactionException ex) {
        return new ResponseEntity<>(parseException(ex), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ErrorDto> handleUnauthorizedUserException(UnauthorizedUserException ex) {
        return new ResponseEntity<>(parseException(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorDto> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        return new ResponseEntity<>(parseException(ex), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(new ErrorDto().message("Invalid payload").details(ex.getMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }





    private ErrorDto parseException(ApplicationException ex) {
        return new ErrorDto().message(ex.getMessage()).details(ex.getDetails());
    }

}
