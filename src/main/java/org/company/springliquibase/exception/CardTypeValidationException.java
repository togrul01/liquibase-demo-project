package org.company.springliquibase.exception;

public class CardTypeValidationException extends RuntimeException {
    public CardTypeValidationException(String message) {
        super(message);
    }
}
