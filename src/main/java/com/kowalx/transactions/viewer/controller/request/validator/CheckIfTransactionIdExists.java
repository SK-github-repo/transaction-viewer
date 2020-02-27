package com.kowalx.transactions.viewer.controller.request.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = TransactionIdValidator.class)
@Documented
public @interface CheckIfTransactionIdExists {
    String message() default "Conflict. Transaction id already exists.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
