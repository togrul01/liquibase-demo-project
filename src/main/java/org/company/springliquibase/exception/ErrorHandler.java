package org.company.springliquibase.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.company.springliquibase.enums.ExceptionMessage.UNEXPECTED_ERROR;
import static org.company.springliquibase.enums.ExceptionMessage.USER_NOT_FOUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error("ActionLog.handleException.error", e);
        return new ErrorResponse(UNEXPECTED_ERROR.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleException(UserNotFoundException e) {
        log.error("ActionLog.handleException.error", e);
        return new ErrorResponse(USER_NOT_FOUND.getMessage());
    }
}
