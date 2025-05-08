package org.company.springliquibase.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCriteria {
    private String cardNumber;
    private String cardholderName;
    private String cardType;
    private String cardBrand;
    private String status;
}