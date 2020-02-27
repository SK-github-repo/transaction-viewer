package com.kowalx.transactions.viewer.controller;

import com.kowalx.transactions.viewer.controller.response.Response;
import com.kowalx.transactions.viewer.dto.CurrencyDTO;
import com.kowalx.transactions.viewer.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping
    public Response addCurrency(@Valid @RequestBody CurrencyDTO newCurrency) {
        log.info(String.format("Adding new currency: %s.", newCurrency));
        currencyService.addCurrency(newCurrency);
        return new Response(HttpStatus.OK.value(), "Currency added successfully.");
    }
}
