package org.company.springliquibase.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCriteria {

    private String cardName;

    private String cardNumber;

    private Date expirationDateFrom;

    private Date expirationDateTo;
}