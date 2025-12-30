package org.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates email addresses with a comprehensive regex pattern.
 */
public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    @Override
    public void initialize(ValidEmail annotation) {
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // @NotNull handles null validation
        }
        
        return value.matches(EMAIL_PATTERN);
    }
}
