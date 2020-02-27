package com.kowalx.transactions.viewer.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kowalx.transactions.viewer.controller.request.serializer.LocalDateDeserializer;
import com.kowalx.transactions.viewer.controller.request.serializer.LocalDateSerializer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class TransactionDTO {

    @NonNull
    private long id;

    @NonNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate bookingDate;

    @NonNull
    private String mainTitle;

    @NonNull
    private BigDecimal amount;

    @NonNull
    private String currency;

    @Setter
    private BigDecimal exchangedAmount;

    @Setter
    private String exchangeCurrency;
}
