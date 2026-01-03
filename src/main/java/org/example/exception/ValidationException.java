package org.example.exception;

import jakarta.validation.ConstraintViolation;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {
    private final Map<String, String> violations;
    
    public ValidationException(String message) {
        super(message);
        this.violations = Map.of();
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.violations = Map.of();
    }
    
    public ValidationException(String message, Map<String, String> violations) {
        super(message);
        this.violations = violations;
    }
    
    public static <T> ValidationException fromConstraintViolations(jakarta.validation.ConstraintViolationException ex) {
        Map<String, String> violations = ex.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage,
                (existing, replacement) -> existing + "; " + replacement
            ));
        
        String message = "Validation failed: " + violations.entrySet().stream()
            .map(entry -> entry.getKey() + " - " + entry.getValue())
            .collect(Collectors.joining(", "));
            
        return new ValidationException(message, violations);
    }
    
    public Map<String, String> getViolations() {
        return violations;
    }
}
