package org.company.springliquibase.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.criteria.UserCriteria;
import org.springframework.data.jpa.domain.Specification;

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

        if (criteria.getUserName() != null && !criteria.getUserName().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("userName")), "%" + criteria.getUserName().toLowerCase() + "%"));
        }

        if (criteria.getAge() != null) {
            predicates.add(cb.equal(root.get("age"), criteria.getAge()));
        }

        if (criteria.getBirthPlace() != null && !criteria.getBirthPlace().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("birthPlace")), "%" + criteria.getBirthPlace().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
} 