package com.kowalx.transactions.viewer.controller.request.validator;

import com.kowalx.transactions.viewer.dto.ExchangeRateDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DateValidator implements ConstraintValidator<ValidateDateRanges, List<ExchangeRateDTO>> {

    private static final String DATE_MISSING_ERROR = "Date in exchange rate range is missing. Please check if all exchange ranges have set start and end date.";
    private static final String DATE_COLLISION_ERROR = "Date collision found in provided file. Correct date ranges to import the file.";

    @Override
    public void initialize(ValidateDateRanges constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<ExchangeRateDTO> values, ConstraintValidatorContext validatorContext) {
        boolean isDateCollision = false;

        List<ExchangeRateDTO> ratesWithNullDates = getReceivedRatesWithNullDates(values);

        if (!ratesWithNullDates.isEmpty()) {
            validatorContext.disableDefaultConstraintViolation();
            validatorContext
                    .buildConstraintViolationWithTemplate(DATE_MISSING_ERROR)
                    .addConstraintViolation();
            return false;
        }

        for (ExchangeRateDTO r : values) {
            isDateCollision = isDateCollisionInImportedRates(r, values);
            if (isDateCollision) {
                break;
            }
        }

        if (isDateCollision) {
            validatorContext.disableDefaultConstraintViolation();
            validatorContext
                    .buildConstraintViolationWithTemplate(DATE_COLLISION_ERROR)
                    .addConstraintViolation();
        }

        return !isDateCollision;
    }

    private List<ExchangeRateDTO> getReceivedRatesWithNullDates(List<ExchangeRateDTO> values) {
        return values.stream()
                .filter(d -> d.getStartDate() == null || d.getEndDate() == null)
                .collect(Collectors.toList());
    }

    private boolean isDateCollisionInImportedRates(ExchangeRateDTO exchangeRate, List<ExchangeRateDTO> exchangeRateList) {
        for (ExchangeRateDTO er : exchangeRateList) {
            if (!exchangeRate.equals(er)) {
                if (isDateCollisionWithOtherRanges(exchangeRate, er)) return true;
            }
        }
        return false;
    }

    private boolean isDateCollisionWithOtherRanges(ExchangeRateDTO exchangeRate, ExchangeRateDTO er) {
        if (isSingleDateInOtherRange(exchangeRate.getStartDate(), er) || isSingleDateInOtherRange(exchangeRate.getEndDate(), er)) {
            return true;
        }
        return isRangeInOtherRange(exchangeRate, er);
    }

    private boolean isSingleDateInOtherRange(LocalDate validatedDate, ExchangeRateDTO exchangeRate) {
        return validatedDate.isAfter(exchangeRate.getStartDate()) && validatedDate.isBefore(exchangeRate.getEndDate());
    }

    private boolean isRangeInOtherRange(ExchangeRateDTO validatedRange, ExchangeRateDTO exchangeRate) {
        return validatedRange.getStartDate().isAfter(exchangeRate.getStartDate()) && validatedRange.getEndDate().isBefore(exchangeRate.getEndDate());
    }
}
