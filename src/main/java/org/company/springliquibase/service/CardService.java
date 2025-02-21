package org.company.springliquibase.service;

import org.company.springliquibase.model.criteria.CardCriteria;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.company.springliquibase.model.response.PageableCardResponse;

import java.util.List;

public interface CardService {

    void createCard(CardRequest request);

    CardResponse getCard(Long cardId);

    CardResponse getCard(String cardNumber);

    List<CardResponse> getCards();

    PageableCardResponse getCards(PageCriteria pageCriteria, CardCriteria cardCriteria);

    void deleteCard(Long cardId);

    void updateCard(Long cardId, CardRequest request);

    void increaseCardBalances();

}