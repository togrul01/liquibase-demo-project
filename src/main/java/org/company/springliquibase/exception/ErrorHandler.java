package org.company.springliquibase.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.company.springliquibase.constants.ExceptionConstants.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final String HANDLE_EXCEPTION_ERROR = "ActionLog.handleException.error";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(UNEXPECTED_EXCEPTION.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(USER_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNotFoundException e) {
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(CARD_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("Error occurred: {}", ex.getMessage());
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }
}
