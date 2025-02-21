package org.company.springliquibase.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public record ErrorResponse(String message) {
}
