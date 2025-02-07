package org.company.springliquibase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
    UNEXPECTED_ERROR("An unexpected error occurred. Please try again later."),
    USER_NOT_FOUND("User not found.");

    private final String message;
}
