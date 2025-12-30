package org.example.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validator for email addresses.
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidEmailValidator.class)
public @interface ValidEmail {
    String message() default "Invalid email format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
