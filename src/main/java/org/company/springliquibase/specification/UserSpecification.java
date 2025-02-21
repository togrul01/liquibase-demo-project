package org.company.springliquibase.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.criteria.UserCriteria;
import org.company.springliquibase.util.PredicateUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;

import static org.company.springliquibase.constants.CriteriaConstants.AGE;
import static org.company.springliquibase.constants.CriteriaConstants.BIRTH_PLACE;
import static org.company.springliquibase.util.PredicateUtil.applyLikePattern;

@AllArgsConstructor
public class UserSpecification implements Specification<UserEntity>, Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    private transient UserCriteria userCriteria;

    @Override
    public Predicate toPredicate(@Nullable Root<UserEntity> root, CriteriaQuery<?> query,
                                 @Nullable CriteriaBuilder cb) {
        if (root == null || cb == null) {
            throw new IllegalArgumentException("Root and CriteriaBuilder must not be null");
        }
        var predicates = PredicateUtil.builder()
                .addNullSafety(userCriteria.getBirthPlace(),
                        birthPlace -> cb.like(root.get(BIRTH_PLACE.getStringValue()), applyLikePattern(birthPlace)))
                .addNullSafety(userCriteria.getAgeFrom(),
                        ageFrom -> cb.greaterThanOrEqualTo(root.get(AGE.getStringValue()), ageFrom))
                .addNullSafety(userCriteria.getAgeTo(),
                        ageTo -> cb.lessThanOrEqualTo(root.get(AGE.getStringValue()), ageTo))
                .build();
        return cb.and(predicates);
    }
}
