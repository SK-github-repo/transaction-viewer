package com.kowalx.transactions.viewer.service;

import com.kowalx.transactions.viewer.dao.CurrencyRepository;
import com.kowalx.transactions.viewer.entity.Currency;
import com.kowalx.transactions.viewer.dto.CurrencyDTO;
import com.kowalx.transactions.viewer.exception.custom.CurrencySymbolExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String CURRENCY_EXISTS_ERROR = "Provided currency symbol already exists: %s.";

    private final CurrencyRepository currencyRepository;

    @Transactional
    public void addCurrency(CurrencyDTO newCurrency) {

        if (currencyRepository.findBySymbol(newCurrency.getSymbol()).isPresent()) {
            log.error(CURRENCY_EXISTS_ERROR + newCurrency.getSymbol());
            throw new CurrencySymbolExistsException(String.format(CURRENCY_EXISTS_ERROR, newCurrency.getSymbol()));
        }

        currencyRepository.save(new Currency(newCurrency.getName(), newCurrency.getSymbol().toUpperCase()));
        log.info("Currency saved successfully.");
    }

    @Transactional
    public Optional<Currency> getCurrencyBySymbol(String currencySymbol) {
        return currencyRepository.findBySymbol(currencySymbol);
    }
}
