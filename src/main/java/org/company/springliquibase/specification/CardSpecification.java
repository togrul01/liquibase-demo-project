package org.company.springliquibase.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import jakarta.persistence.criteria.Predicate;
import org.company.springliquibase.entity.CardEntity;
import org.company.springliquibase.model.criteria.CardCriteria;
import org.company.springliquibase.util.CardPredicateUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import java.io.Serial;
import java.io.Serializable;

import static org.company.springliquibase.constants.CriteriaConstants.*;

@AllArgsConstructor
public class CardSpecification implements Specification<CardEntity>, Serializable {
    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    private transient CardCriteria cardCriteria;

    public static Specification<CardEntity> active() {
        return (root, query, cb) -> cb.equal(root.get("status"), "ACTIVE");
    }

    @Override
    public Predicate toPredicate(@Nullable Root<CardEntity> root, @Nullable CriteriaQuery<?> query,
                                 @Nullable CriteriaBuilder cb) {
        if (root == null || cb == null) {
            throw new IllegalArgumentException("Root and CriteriaBuilder must not be null");
        }

        var predicates = CardPredicateUtil.builder()
                .addNullSafety(cardCriteria.getCardholderName(),
                        cardholderName -> cb.like(root.get("cardholderName"), "%" + cardholderName + "%"))
                .addNullSafety(cardCriteria.getCardNumber(),
                        cardNumber -> cb.like(root.get("cardNumber"), "%" + cardNumber + "%"))
                .addNullSafety(cardCriteria.getCardType(),
                        cardType -> cb.equal(root.get("cardType"), cardType))
                .addNullSafety(cardCriteria.getCardBrand(),
                        cardBrand -> cb.equal(root.get("cardBrand"), cardBrand))
                .addNullSafety(cardCriteria.getStatus(),
                        status -> cb.equal(root.get("status"), status))
                .build();

        return cb.and(predicates);
    }
}
