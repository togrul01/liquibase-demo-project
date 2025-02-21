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



    @Override
    public Predicate toPredicate(@Nullable Root<CardEntity> root, @Nullable CriteriaQuery<?> query,
                                 @Nullable CriteriaBuilder cb) {
        if (root == null || cb == null) {
            throw new IllegalArgumentException("Root and CriteriaBuilder must not be null");
        }

        var predicates = CardPredicateUtil.builder()
                .addNullSafety(cardCriteria.getCardName(),
                        cardName -> cb.equal(root.get(CARD_NAME.getStringValue()), cardName))
                .addNullSafety(cardCriteria.getCardNumber(),
                        cardNumber -> cb.like(root.get(CARD_NUMBER.getStringValue()), "%" + cardNumber + "%"))
                .addNullSafety(cardCriteria.getExpirationDateFrom(),
                        expirationDateFrom -> cb.greaterThanOrEqualTo(root.get
                                        (String.valueOf(EXPIRATION_DATE_FROM)),
                                expirationDateFrom))
                .addNullSafety(cardCriteria.getExpirationDateTo(),
                        expirationDateTo -> cb.lessThanOrEqualTo(root.get
                                (String.valueOf(EXPIRATION_DATE_TO)), expirationDateTo))
                .build();

        return cb.and(predicates);
    }
}
