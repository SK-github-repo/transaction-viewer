package com.kowalx.transactions.viewer.controller.request.validator;

import com.kowalx.transactions.viewer.dao.TransactionRepository;
import com.kowalx.transactions.viewer.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TransactionIdValidator implements ConstraintValidator<CheckIfTransactionIdExists, List<TransactionDTO>> {

    private final TransactionRepository transactionRepository;

    @Override
    public void initialize(CheckIfTransactionIdExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<TransactionDTO> value, ConstraintValidatorContext context) {

        int numOfTransactions = value.size();
        Set<Long> numOfTransactionIds = value.stream().map(TransactionDTO::getId).collect(Collectors.toSet());
        if (numOfTransactions != numOfTransactionIds.size()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Incorrect file." +
                            " Transaction id defined multiple times in file. Correct id's and try again.")
                    .addConstraintViolation();
            return false;
        }

        List<TransactionDTO> alreadyExistingIds = value.stream()
                .filter(t -> transactionRepository.findByTransactionId(t.getId()).isPresent())
                .collect(Collectors.toList());

        if (!alreadyExistingIds.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Transaction Id's already existing in DB: " +
                            alreadyExistingIds.stream().map(TransactionDTO::getId).collect(Collectors.toList()) +
                            ". Correct id's and try again.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
