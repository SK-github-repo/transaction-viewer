package com.kowalx.transactions.viewer.controller;

import com.kowalx.transactions.viewer.controller.request.ReceivedExchangeRates;
import com.kowalx.transactions.viewer.controller.response.Response;
import com.kowalx.transactions.viewer.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/exchange")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @PostMapping
    public Response addExchangeRates(@Valid @RequestBody ReceivedExchangeRates receivedExchangeRates) {
        log.info("Adding exchange rates.");
        log.debug("Adding exchange rates: " + receivedExchangeRates);
        exchangeRateService.addExchangeRates(receivedExchangeRates);
        return new Response(HttpStatus.OK.value(), "Exchange rates imported successfully.");
    }
}
