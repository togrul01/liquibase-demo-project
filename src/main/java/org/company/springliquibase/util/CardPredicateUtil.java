package org.company.springliquibase.util;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CardPredicateUtil {
    private final List<Predicate> predicateList;

    public CardPredicateUtil() {
        this.predicateList = new ArrayList<>();
    }

    public static CardPredicateUtil builder() {
        return new CardPredicateUtil();
    }

    public <T> CardPredicateUtil add(T object, Function<T, Predicate> function) {
        predicateList.add(function.apply(object));
        return this;
    }

    public <T> CardPredicateUtil addNullSafety(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicateList.add(function.apply(object));
        }
        return this;
    }

    public Predicate[] build() {
        return predicateList.toArray(new Predicate[0]);
    }
}