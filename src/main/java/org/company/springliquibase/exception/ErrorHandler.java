package org.company.springliquibase.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.company.springliquibase.constants.ExceptionConstants.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final String HANDLE_EXCEPTION_ERROR = "ActionLog.handleException.error";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return new ErrorResponse(UNEXPECTED_EXCEPTION.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleException(UserNotFoundException e) {
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return new ErrorResponse(USER_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleException(CardNotFoundException e) {
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return new ErrorResponse(CARD_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("Error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
