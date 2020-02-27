package com.kowalx.transactions.viewer.exception;

import com.kowalx.transactions.viewer.exception.custom.CurrencySymbolExistsException;
import com.kowalx.transactions.viewer.exception.custom.MissingCurrencyException;
import com.kowalx.transactions.viewer.exception.custom.MissingExchangeRateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> missingCurrency(MissingCurrencyException missingCurrencyException) {
        ErrorResponse errorResponse;

        errorResponse = buildErrorResponse(missingCurrencyException, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> missingExchangeRate(MissingExchangeRateException missingExchangeRateException) {
        ErrorResponse errorResponse;

        errorResponse = buildErrorResponse(missingExchangeRateException, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> currencyExists(CurrencySymbolExistsException currencyException) {
        ErrorResponse errorResponse;

        errorResponse = buildErrorResponse(currencyException, HttpStatus.CONFLICT);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse;

        errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), LocalDateTime.now(),
                exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse errorResponse;

        errorResponse = buildErrorResponse(exception, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse buildErrorResponse(Exception exception, HttpStatus httpStatus) {
        return new ErrorResponse(httpStatus.value(), LocalDateTime.now(), exception.getMessage());
    }
}
