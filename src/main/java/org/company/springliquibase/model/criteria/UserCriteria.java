package org.company.springliquibase.model.criteria;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCriteria {
    private String userName;
    private Integer age;
    private String birthPlace;
}