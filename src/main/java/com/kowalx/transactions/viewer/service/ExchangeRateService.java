package com.kowalx.transactions.viewer.service;

import com.kowalx.transactions.viewer.controller.request.ReceivedExchangeRates;
import com.kowalx.transactions.viewer.dao.ExchangeRateRepository;
import com.kowalx.transactions.viewer.dto.ExchangeRateDTO;
import com.kowalx.transactions.viewer.entity.Currency;
import com.kowalx.transactions.viewer.entity.ExchangeRate;
import com.kowalx.transactions.viewer.entity.Transaction;
import com.kowalx.transactions.viewer.exception.custom.MissingCurrencyException;
import com.kowalx.transactions.viewer.exception.custom.MissingExchangeRateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private static final String MISSING_CURRENCY_EXCEPTION = "Provided currency: %s is missing in currency table. " +
            "Add it to currency table to successfully import exchange rate for it.";

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyService currencyService;

    @Transactional
    public void addExchangeRates(ReceivedExchangeRates newExchangeRates) {
        newExchangeRates.getReceivedExchangeRates().forEach(this::saveExchangeRate);
        log.info("Exchange rates imported successfully.");
    }

    //TODO - date collision should be also verified before saving to DB - not only in the input file as in the requirements
    private void saveExchangeRate(ExchangeRateDTO newExchangeRate) {
        log.debug("Saving exchange rate: " + newExchangeRate);

        Optional<Currency> currencyFrom = currencyService.getCurrencyBySymbol(newExchangeRate.getCurrencyFrom());
        Optional<Currency> currencyTo = currencyService.getCurrencyBySymbol(newExchangeRate.getCurrencyTo());

        if (currencyFrom.isEmpty()) {
            log.error(String.format(MISSING_CURRENCY_EXCEPTION, newExchangeRate.getCurrencyFrom()));
            throw new MissingCurrencyException(String.format(MISSING_CURRENCY_EXCEPTION, newExchangeRate.getCurrencyFrom()));
        }

        if (currencyTo.isEmpty()) {
            log.error(String.format(MISSING_CURRENCY_EXCEPTION, newExchangeRate.getCurrencyTo()));
            throw new MissingCurrencyException(String.format(MISSING_CURRENCY_EXCEPTION, newExchangeRate.getCurrencyTo()));
        }

        ExchangeRate exchangeRate = new ExchangeRate(newExchangeRate.getStartDate(),
                newExchangeRate.getEndDate(),
                newExchangeRate.getExchangeRate(),
                currencyFrom.get(),
                currencyTo.get());

        exchangeRateRepository.save(exchangeRate);
        log.debug("Exchange rate saved successfully.");
    }

    @Transactional
    public BigDecimal getExchangeRateForTransaction(Transaction transaction, String exchangeCurrency) {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();

        List<ExchangeRate> exchangeRatesForDate = getExchangeRateForDate(exchangeRates, transaction.getBookingDate());
        if (exchangeRatesForDate.isEmpty()) {
            log.error(("No exchange rate found for given date:" + transaction.getBookingDate()));
            return BigDecimal.valueOf(0);
            //TODO - verify if the list should be returned if there is no exchange rate for the transaction
//            throw new MissingExchangeRateException("No exchange rate found for given date:" + transaction.getBookingDate());
        }

        ExchangeRate exchangeRateForCurrency = getExchangeRateForCurrency(exchangeRatesForDate, exchangeCurrency, transaction.getCurrency().getSymbol());

        return exchangeRateForCurrency.getExchangeRate();
    }

    private ExchangeRate getExchangeRateForCurrency(List<ExchangeRate> exchageRatesForGivenDay, String exchangeCurrency, String paymentCurrency) {
        List<ExchangeRate> result = exchageRatesForGivenDay
                .stream()
                .filter(e -> paymentCurrency.equalsIgnoreCase(e.getCurrencyFrom().getSymbol()) && exchangeCurrency.equalsIgnoreCase(e.getCurrencyTo().getSymbol()))
                .collect(Collectors.toList());
        //TODO - throw exception if the list has more than 2 elements - after implementing the "date collision" validation for records in DB
        if (result.isEmpty()) {
            log.error("Exchange rate not found for currency relation: " + paymentCurrency + " - " + exchangeCurrency);
            throw new MissingExchangeRateException("Exchange rate not found for currency relation: " + paymentCurrency + " - " + exchangeCurrency);
        } else {
            return result.get(0);
        }
    }

    private List<ExchangeRate> getExchangeRateForDate(List<ExchangeRate> exchangeRates, LocalDate date) {
        return exchangeRates
                .stream()
                .filter(e -> date.isAfter(e.getStartDate()) && date.isBefore(e.getEndDate())).collect(Collectors.toList());
    }
}
