package org.company.springliquibase.exception;

public class DuplicateCardNumberException extends RuntimeException {
    public DuplicateCardNumberException(String message) {
        super(message);
    }
}
