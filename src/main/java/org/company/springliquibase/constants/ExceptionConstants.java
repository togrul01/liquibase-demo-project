package org.company.springliquibase.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionConstants {
    CARD_NOT_FOUND("CARD_NOT_FOUND", "Card not found."),
    USER_NOT_FOUND("User not found.", "User not found."),
    CLIENT_ERROR("CLIENT_EXCEPTION", "An error occurred from the client source."),
    UNEXPECTED_EXCEPTION("UNEXPECTED_EXCEPTION", "An unexpected error occurred."),
    EMPTY_CARD_NUMBER("EMPTY_CARD_NUMBER", "Card number cannot be empty."),
    INVALID_CARD_NUMBER("INVALID_CARD_NUMBER", "Invalid card number."),
    EMPTY_CARDHOLDER_NAME("EMPTY_CARDHOLDER_NAME", "Cardholder name cannot be empty."),
    EMPTY_CVV("EMPTY_CVV", "CVV cannot be empty."),
    EMPTY_CARD_TYPE("EMPTY_CARD_TYPE", "Card type cannot be empty."),
    INVALID_CARD_TYPE("INVALID_CARD_TYPE", "Invalid card type."),
    EXPIRED_DATE("EXPIRED_DATE", "Expiration date cannot be in the past."),
    NEGATIVE_BALANCE("NEGATIVE_BALANCE", "Balance cannot be negative."),
    DUPLICATE_CARD_NUMBER("DUPLICATE_CARD_NUMBER", "This card number is already registered.");

    private final String code;
    private final String message;
}
