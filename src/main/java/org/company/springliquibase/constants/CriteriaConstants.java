package org.company.springliquibase.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CriteriaConstants {
    PAGE_DEFAULT_VALUE(0),
    COUNT_DEFAULT_VALUE(10),
    AGE("age"),
    BIRTH_PLACE("birthPlace");

    private final Object value;

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
