package org.company.springliquibase.service;

import org.company.springliquibase.model.response.Response;
import org.company.springliquibase.model.criteria.CardCriteria;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.company.springliquibase.model.response.PageableCardResponse;
import org.springframework.http.ResponseEntity;

public interface CardService {

    ResponseEntity<Response<CardResponse>> createCard(CardRequest request);

    ResponseEntity<Response<CardResponse>> getCard(Long cardId);

    ResponseEntity<Response<CardResponse>> getCard(String cardNumber);

    PageableCardResponse getCards(PageCriteria pageCriteria, CardCriteria cardCriteria);

    ResponseEntity<Response<String>> deleteCard(Long cardId);

    void increaseCardBalances();

    void increaseCardBalancesWithJpa();

    ResponseEntity<Response<PageableCardResponse>> findAll(String name, int page, int size);

}