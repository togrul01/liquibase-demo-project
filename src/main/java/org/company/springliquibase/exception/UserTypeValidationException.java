package org.company.springliquibase.exception;

public class UserTypeValidationException extends RuntimeException {
    public UserTypeValidationException(String message) {
        super(message);
    }
} 