package com.kowalx.transactions.viewer.exception.custom;

public class MissingExchangeRateException extends RuntimeException {
    public MissingExchangeRateException(String message) {
        super(message);
    }
}
