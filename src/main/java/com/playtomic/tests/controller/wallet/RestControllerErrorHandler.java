package com.playtomic.tests.controller.wallet;

import com.playtomic.tests.controller.wallet.model.ErrorDto;
import com.playtomic.tests.exception.*;
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

    @ExceptionHandler(UnprocessableTransactionException.class)
    public ResponseEntity<ErrorDto> handlePaymentTransactionException(UnprocessableTransactionException ex) {
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


    private ErrorDto parseException(BaseException ex) {
        return new ErrorDto().message(ex.getMessage()).details(ex.getDetails());
    }
}
