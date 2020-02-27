package com.kowalx.transactions.viewer.exception.custom;

public class CurrencySymbolExistsException extends RuntimeException {
    public CurrencySymbolExistsException(String message) {
        super(message);
    }
}
