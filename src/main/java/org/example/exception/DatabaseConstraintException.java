package org.example.exception;

/**
 * Exception thrown when a database constraint is violated (e.g., duplicate key, foreign key violation).
 */
public class DatabaseConstraintException extends RuntimeException {
    private final String constraintName;
    private final String violatedValue;
    
    public DatabaseConstraintException(String message) {
        super(message);
        this.constraintName = null;
        this.violatedValue = null;
    }

    public DatabaseConstraintException(String message, Throwable cause) {
        super(message, cause);
        this.constraintName = null;
        this.violatedValue = null;
    }
    
    public DatabaseConstraintException(String message, String constraintName, String violatedValue) {
        super(message);
        this.constraintName = constraintName;
        this.violatedValue = violatedValue;
    }
    
    public String getConstraintName() {
        return constraintName;
    }
    
    public String getViolatedValue() {
        return violatedValue;
    }
    
    /**
     * Creates a DatabaseConstraintException from a Hibernate constraint violation.
     */
    public static DatabaseConstraintException fromHibernateConstraintViolation(
            org.hibernate.exception.ConstraintViolationException ex) {
        
        String sqlMessage = ex.getSQLException().getMessage();
        
        // Parse duplicate key error: "Duplicate entry 'value' for key 'table.column'"
        if (sqlMessage.contains("Duplicate entry")) {
            String value = extractBetween(sqlMessage, "Duplicate entry '", "' for key");
            String constraint = extractBetween(sqlMessage, "for key '", "'");
            String fieldName = constraint != null && constraint.contains(".") 
                ? constraint.substring(constraint.lastIndexOf('.') + 1) 
                : constraint;
            
            return new DatabaseConstraintException(
                String.format("Duplicate value '%s' for field '%s'", value, fieldName),
                constraint,
                value
            );
        }
        
        // Parse foreign key error
        if (sqlMessage.contains("foreign key constraint")) {
            String constraint = extractBetween(sqlMessage, "CONSTRAINT `", "`");
            return new DatabaseConstraintException(
                "Foreign key constraint violation: " + constraint,
                constraint,
                null
            );
        }
        
        // Generic constraint violation
        return new DatabaseConstraintException(
            "Database constraint violation: " + sqlMessage,
            ex
        );
    }
    
    private static String extractBetween(String source, String start, String end) {
        try {
            int startIdx = source.indexOf(start);
            if (startIdx == -1) return null;
            startIdx += start.length();
            int endIdx = source.indexOf(end, startIdx);
            if (endIdx == -1) return null;
            return source.substring(startIdx, endIdx);
        } catch (Exception e) {
            return null;
        }
    }
}
