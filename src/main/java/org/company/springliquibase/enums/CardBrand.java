package org.company.springliquibase.enums;

import lombok.Getter;

@Getter
public enum CardBrand {
    VISA("4"),
    MASTERCARD("5"),
    AMEX("34"),
    DISCOVER("6011");

    private final String prefix;

    CardBrand(String prefix) {
        this.prefix = prefix;
    }

}
