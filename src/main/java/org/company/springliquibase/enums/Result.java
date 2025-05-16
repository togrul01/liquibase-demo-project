package org.company.springliquibase.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Result {
    SUCCESS(200, "success"),
    ERROR(500, "error"),
    CARD_CREATED(100, "card.created"),
    CARD_UPDATED(101, "card.updated"),
    CARD_DELETED(102, "card.deleted"),
    CARD_FOUND(103, "card.found"),
    CARD_NOT_FOUND(200, "card.not.found"),
    CARD_CREATION_ERROR(201, "card.creation.error"),
    CARD_UPDATE_ERROR(202, "card.update.error"),
    CARD_DELETE_ERROR(203, "card.delete.error"),
    CARD_VALIDATION_ERROR(204, "card.validation.error"),
    USER_CREATED(201, "user.created"),
    USER_FOUND(200, "user.found"),
    USER_NOT_FOUND(404, "user.not.found"),
    USER_DELETED(200, "user.deleted"),
    USER_DELETE_ERROR(500, "user.delete.error"),
    USER_UPDATED(104, "User updated successfully");

    private final int code;
    private final String message;
} 