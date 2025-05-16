package org.company.springliquibase.exception;

public class CardCreationException extends RuntimeException {
    public CardCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardCreationException(String message) {
        super(message);
    }
}