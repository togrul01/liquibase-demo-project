package org.company.springliquibase.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.company.springliquibase.util.LocalizationUtil.getLocalizedMessageByKey;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final String HANDLE_EXCEPTION_ERROR = "ActionLog.handleException.error";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        var message = getLocalizedMessageByKey("i18n/error", "unexpected.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(message));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        var message = getLocalizedMessageByKey("i18n/error", "user.not.found.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(message));
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNotFoundException e) {
        var message = getLocalizedMessageByKey("i18n/error", "card.not.found.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(message));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        var message = getLocalizedMessageByKey("i18n/error", "user.already.exist.exception");
        log.error("Error occurred: {}", ex.getMessage());
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(message));
    }
}
