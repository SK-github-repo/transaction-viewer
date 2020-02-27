package com.kowalx.transactions.viewer.exception.custom;

public class MissingCurrencyException extends RuntimeException {
    public MissingCurrencyException(String message) {
        super(message);
    }
}
