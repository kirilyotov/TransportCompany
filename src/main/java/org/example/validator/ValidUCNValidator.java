package org.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates Bulgarian UCN (Unique Citizen Number - ЕГН).
 * UCN format: 10 digits with check digit validation.
 */
public class ValidUCNValidator implements ConstraintValidator<ValidUCN, String> {
    private static final int UCN_LENGTH = 10;
    private static final int[] WEIGHTS = {2, 4, 8, 5, 10, 9, 7, 3, 6};
    
    @Override
    public void initialize(ValidUCN annotation) {
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // @NotNull handles null validation
        }
        
        // Check if it's exactly 10 digits
        if (!value.matches("^\\d{" + UCN_LENGTH + "}$")) {
            return false;
        }
        
        // Validate check digit
        return validateCheckDigit(value, context);
    }
    
    private boolean validateCheckDigit(String ucn, ConstraintValidatorContext context) {
        int sum = 0;
        
        // Calculate weighted sum of first 9 digits
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(ucn.charAt(i)) * WEIGHTS[i];
        }
        
        // Calculate check digit
        int checkDigit = sum % 11;
        if (checkDigit == 10) {
            checkDigit = 0;
        }
        
        int lastDigit = Character.getNumericValue(ucn.charAt(9));
        
        if (checkDigit != lastDigit) {
            addConstraintViolation(context, "Invalid UCN check digit.");
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
