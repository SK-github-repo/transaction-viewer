package com.kowalx.transactions.viewer.controller.request;

import com.kowalx.transactions.viewer.controller.request.validator.ValidateDateRanges;
import com.kowalx.transactions.viewer.dto.ExchangeRateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedExchangeRates {

    @NotNull
    @ValidateDateRanges
    private List<ExchangeRateDTO> receivedExchangeRates;
}
