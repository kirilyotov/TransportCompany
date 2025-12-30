package org.example.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that an amount is positive (greater than 0).
 * Works with BigDecimal amounts for financial calculations.
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidAmountValidator.class)
public @interface ValidAmount {
    String message() default "Amount must be greater than 0!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
