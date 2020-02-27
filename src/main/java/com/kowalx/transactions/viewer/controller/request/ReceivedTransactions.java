package com.kowalx.transactions.viewer.controller.request;

import com.kowalx.transactions.viewer.controller.request.validator.CheckIfTransactionIdExists;
import com.kowalx.transactions.viewer.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedTransactions {

    @CheckIfTransactionIdExists
    private List<TransactionDTO> receivedTransactions;
}
