package com.kowalx.transactions.viewer.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class CurrencyDTO {

    @NotNull(message = "Missing currency name")
    private final String name;

    @NotNull(message = "Missing currency symbol")
    @Pattern(regexp = "^[A-Za-z]{3}$", message = "Symbol should contain only 3 letters")
    private final String symbol;
}
