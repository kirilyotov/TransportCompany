package org.example.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validator for Bulgarian UCN (Unique Citizen Number).
 * Validates the format and check digit.
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidUCNValidator.class)
public @interface ValidUCN {
    String message() default "Invalid UCN format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
