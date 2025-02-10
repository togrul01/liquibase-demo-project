package org.company.springliquibase.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.PredicateUtils;
import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.criteria.UserCriteria;
import org.company.springliquibase.util.PredicateUtil;
import org.springframework.data.jpa.domain.Specification;

import static org.company.springliquibase.constants.CriteriaConstants.AGE;
import static org.company.springliquibase.constants.CriteriaConstants.BIRTH_PLACE;
import static org.company.springliquibase.util.PredicateUtil.applyLikePattern;

@AllArgsConstructor
public class UserSpecification implements Specification<UserEntity> {
    private UserCriteria userCriteria;

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var predicates = PredicateUtil.builder()
                .addNullSafety(userCriteria.getBirthPlace(),
                        birthPlace -> cb.like(root.get(BIRTH_PLACE), applyLikePattern(birthPlace)))
                .addNullSafety(userCriteria.getAgeFrom(),
                        ageFrom -> cb.greaterThanOrEqualTo(root.get(AGE), ageFrom))
                .addNullSafety(userCriteria.getAgeTo(),
                        ageTo -> cb.lessThanOrEqualTo(root.get(AGE), ageTo))
                .build();
        return cb.and(predicates);
    }
}
