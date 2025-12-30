package org.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Validates that amounts are positive (greater than 0).
 * Used for financial values like price, salary, payment amounts.
 */
public class ValidAmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {
    
    @Override
    public void initialize(ValidAmount annotation) {
    }
    
    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull handles null validation
        }
        
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            addConstraintViolation(context, "Amount must be greater than 0");
            return false;
        }
        
        // Optional: Check if amount doesn't exceed 999,999.99
        if (value.compareTo(new BigDecimal("999999.99")) > 0) {
            addConstraintViolation(context, "Amount exceeds maximum allowed value");
            return false;
        }
        
        return true;
    }
    
    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
