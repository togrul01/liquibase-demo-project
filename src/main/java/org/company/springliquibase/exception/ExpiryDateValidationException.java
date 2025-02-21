package org.company.springliquibase.exception;

public class ExpiryDateValidationException extends RuntimeException {
    public ExpiryDateValidationException(String message) {
        super(message);
    }
}
