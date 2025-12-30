package org.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates that a name doesn't contain special characters and follows naming conventions.
 * Allows letters, spaces, hyphens, and apostrophes.
 * Must start with a capital letter.
 */
public class InvalidNamesValidator implements ConstraintValidator<InvalidNames, String> {
    private static final String VALID_NAME_PATTERN = "^[A-Z][a-zA-Z\\s'-]*$";
    
    @Override
    public void initialize(InvalidNames annotation) {
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // @NotBlank handles null/empty validation
        }
        
        // Check if name starts with capital letter and contains only valid characters
        if (!value.matches(VALID_NAME_PATTERN)) {
            addConstraintViolation(context, "Name must start with a capital letter and contain only letters, spaces, hyphens, and apostrophes.");
            return false;
        }
        
        // Check if name doesn't have consecutive spaces or special characters
        if (value.matches(".*\\s{2,}.*") || value.matches(".*[-']{2,}.*")) {
            addConstraintViolation(context, "Name cannot have consecutive spaces or special characters.");
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