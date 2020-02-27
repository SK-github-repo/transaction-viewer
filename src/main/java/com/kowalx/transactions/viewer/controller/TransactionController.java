package com.kowalx.transactions.viewer.controller;

import com.kowalx.transactions.viewer.controller.request.ReceivedTransactions;
import com.kowalx.transactions.viewer.controller.response.Response;
import com.kowalx.transactions.viewer.dto.TransactionDTO;
import com.kowalx.transactions.viewer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/all")
    public List<TransactionDTO> getAllTransactions() {
        log.info("Getting all transactions from DB.");
        return transactionService.getAllTransactions();
    }

    @GetMapping("/all/withCurrency")
    public List<TransactionDTO> getAllTransactionsWithCurrencyConversion() {
        log.info("Getting all transactions with currency conversion from DB.");
        return transactionService.getAllTransactionsWithCurrencyConversion();
    }

    @PostMapping
    public Response addTransaction(@Valid @RequestBody ReceivedTransactions receivedTransactions) {
        log.info("Adding transactions.");
        log.debug("Adding transactions: " + receivedTransactions);
        transactionService.addTransactions(receivedTransactions);
        return new Response(HttpStatus.OK.value(), "Transactions added successfully.");
    }
}
