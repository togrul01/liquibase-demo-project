package org.company.springliquibase.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.company.springliquibase.util.LocalizationUtil.getLocalizedMessageByKey;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    private static final String HANDLE_EXCEPTION_ERROR = "ActionLog.handleException.error";
    private static final String ERROR_BUNDLE = "i18n/error";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "unexpected.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(message));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "user.not.found.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(message));
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNotFoundException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "card.not.found.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(message));
    }

    @ExceptionHandler(CardCreationException.class)
    public ResponseEntity<ErrorResponse> handleException(CardCreationException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "card.creation.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(CardTypeValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(CardTypeValidationException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "invalid.card.type.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(CardNumberValidationException.class)
    public ResponseEntity<ErrorResponse> handleException(CardNumberValidationException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "invalid.card.number.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(CvvValidationException.class)
    public ResponseEntity<ErrorResponse> handleException(CvvValidationException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "invalid.cvv.number.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(CardholderNameValidationException.class)
    public ResponseEntity<ErrorResponse> handleException(CardholderNameValidationException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "empty.cardholder.name.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(ExpiryDateValidationException.class)
    public ResponseEntity<ErrorResponse> handleException(ExpiryDateValidationException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "expired.date.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(BalanceValidationException.class)
    public ResponseEntity<ErrorResponse> handleException(BalanceValidationException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "negative.balance.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(DuplicateCardNumberException.class)
    public ResponseEntity<ErrorResponse> handleException(DuplicateCardNumberException e) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "duplicate.card.number.exception");
        log.error(HANDLE_EXCEPTION_ERROR, e);
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(message));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        var message = getLocalizedMessageByKey(ERROR_BUNDLE, "user.already.exist.exception");
        log.error("Error occurred: {}", ex.getMessage());
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(message));
    }
}
