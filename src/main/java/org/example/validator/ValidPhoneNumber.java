package org.example.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validator for phone numbers.
 * Supports Bulgarian phone format and international format.
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidPhoneNumberValidator.class)
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
