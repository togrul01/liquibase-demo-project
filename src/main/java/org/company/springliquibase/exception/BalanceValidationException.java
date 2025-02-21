package org.company.springliquibase.exception;

public class BalanceValidationException extends RuntimeException {
    public BalanceValidationException(String message) {
        super(message);
    }
}
