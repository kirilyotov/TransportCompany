package org.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates phone numbers in Bulgarian format or international format.
 */
public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    // Bulgarian phone format: +359XXXXXXXXX or 0XXXXXXXXX
    private static final String BULGARIAN_PHONE_PATTERN = "^(\\+?359|0)[1-9]\\d{1,9}$";
    
    @Override
    public void initialize(ValidPhoneNumber annotation) {
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // @NotNull handles null validation
        }
        
        return value.matches(BULGARIAN_PHONE_PATTERN);
    }
}
