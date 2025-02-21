package org.company.springliquibase.exception;

public class CvvValidationException extends RuntimeException {
    public CvvValidationException(String message) {
        super(message);
    }
}
