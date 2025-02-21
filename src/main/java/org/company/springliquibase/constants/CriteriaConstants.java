package org.company.springliquibase.constants;


import lombok.Getter;

@Getter
public enum CriteriaConstants {
    AGE("age"),
    BIRTH_PLACE("birthPlace"),
    CARD_NAME("cardName"),
    CARD_NUMBER("cardNumber"),
    PAGE_DEFAULT_VALUE(0),
    COUNT_DEFAULT_VALUE(10),
    EXPIRATION_DATE_FROM("expirationDateFrom"),
    EXPIRATION_DATE_TO("expirationDateTo");

    private final Object value;

    CriteriaConstants(Object value) {
        this.value = value;
    }

    public String getStringValue() {
        if (value instanceof String stringValue) {
            return stringValue;
        }
        throw new IllegalStateException("Value is not a String");
    }

    public Integer getIntegerValue() {
        if (value instanceof Integer intValue) {
            return intValue;
        }
        throw new IllegalStateException("Value is not an Integer");
    }
}
