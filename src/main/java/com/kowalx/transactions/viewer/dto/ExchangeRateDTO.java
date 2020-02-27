package com.kowalx.transactions.viewer.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kowalx.transactions.viewer.controller.request.serializer.LocalDateDeserializer;
import com.kowalx.transactions.viewer.controller.request.serializer.LocalDateSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ExchangeRateDTO {

    @NotNull(message = "Missing start date.")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private final LocalDate startDate;

    @NotNull(message = "Missing end date.")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private final LocalDate endDate;

    @NotNull(message = "Missing exchange rate")
    private final BigDecimal exchangeRate;

    @NonNull
    @Setter
    private String currencyFrom = "EUR";

    @NonNull
    @Setter
    private String currencyTo = "PLN";
}
