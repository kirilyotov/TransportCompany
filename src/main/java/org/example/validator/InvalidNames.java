package org.example.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = InvalidNamesValidator.class)
public @interface InvalidNames {
    String message() default "Invalid name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}