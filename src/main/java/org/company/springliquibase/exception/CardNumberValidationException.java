package org.company.springliquibase.exception;

public class CardNumberValidationException extends RuntimeException {
    public CardNumberValidationException(String message) {
        super(message);
    }
}
