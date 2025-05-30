package org.company.springliquibase.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.criteria.UserCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<UserEntity> {
    private final UserCriteria criteria;

    public UserSpecification(UserCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(criteria.getUserName())) {
            predicates.add(cb.like(cb.lower(root.get("username")), "%" + criteria.getUserName().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(criteria.getUserName())) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getUserName().toLowerCase() + "%"));
        }


        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public static Specification<UserEntity> active() {
        return (root, query, cb) -> cb.notEqual(root.get("status"), "DELETED");
    }
}
