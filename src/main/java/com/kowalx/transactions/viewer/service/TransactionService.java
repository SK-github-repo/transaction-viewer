package com.kowalx.transactions.viewer.service;

import com.kowalx.transactions.viewer.controller.request.ReceivedTransactions;
import com.kowalx.transactions.viewer.dao.TransactionRepository;
import com.kowalx.transactions.viewer.dto.TransactionDTO;
import com.kowalx.transactions.viewer.entity.Currency;
import com.kowalx.transactions.viewer.entity.Transaction;
import com.kowalx.transactions.viewer.exception.custom.MissingCurrencyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    @Transactional
    public void addTransactions(ReceivedTransactions receivedTransactions) {
        receivedTransactions.getReceivedTransactions().forEach(this::saveTransactions);
        log.info("Transactions imported successfully");
    }

    private void saveTransactions(TransactionDTO newTransaction) {
        log.debug("Saving transaction: " + newTransaction);

        Optional<Currency> currency = currencyService.getCurrencyBySymbol(newTransaction.getCurrency());

        if (currency.isEmpty()) {
            throw new MissingCurrencyException("Provided currency: " + newTransaction.getCurrency() +
                    " is missing in currency table. Add it to currency table to successfully import transactions.");
        }

        Transaction transaction = new Transaction(newTransaction.getId(),
                newTransaction.getBookingDate(),
                newTransaction.getMainTitle(),
                newTransaction.getAmount(),
                currency.get());

        transactionRepository.save(transaction);
        log.debug("Transaction saved successfully.");
    }

    @Transactional
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "bookingDate"))
                .stream()
                .map(t -> new TransactionDTO(
                        t.getTransactionId(),
                        t.getBookingDate(),
                        t.getMainTitle(),
                        t.getAmount(),
                        t.getCurrency().getSymbol())
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TransactionDTO> getAllTransactionsWithCurrencyConversion() {
        //TODO - take the exchange currency from user
        String exchangeCurrency = "EUR";
        return transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "bookingDate"))
                .stream()
                .map(t -> {
                            BigDecimal exchangedAmount = convertTransactionAmountToOtherCurrency(t, exchangeCurrency);
                            TransactionDTO transactionDTO = new TransactionDTO(
                                    t.getTransactionId(),
                                    t.getBookingDate(),
                                    t.getMainTitle(),
                                    t.getAmount(),
                                    t.getCurrency().getSymbol());
                            transactionDTO.setExchangedAmount(exchangedAmount);
                            transactionDTO.setExchangeCurrency(exchangeCurrency);
                            return transactionDTO;
                        }
                )
                .collect(Collectors.toList());
    }

    private BigDecimal convertTransactionAmountToOtherCurrency(Transaction t, String exchangeCurrency) {
        log.debug("Converting transaction: " + t + " to currency to " + exchangeCurrency);
        BigDecimal exchangeRateForTransaction = exchangeRateService.getExchangeRateForTransaction(t, exchangeCurrency);
        BigDecimal exchangedAmount = t.getAmount().multiply(exchangeRateForTransaction);
        return exchangedAmount.setScale(2, RoundingMode.CEILING);
    }
}
