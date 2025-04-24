package org.company.springliquibase.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.model.criteria.CardCriteria;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.company.springliquibase.model.response.PageableCardResponse;
import org.company.springliquibase.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {
    private final CardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest cardRequest) {
        cardService.createCard(cardRequest);
        CardResponse createdCard = cardService.getCard(cardRequest.getCardNumber());
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @GetMapping("/{cardId}")
    public CardResponse getCard(@PathVariable Long cardId) {
        return cardService.getCard(cardId);
    }

    @GetMapping
    public List<CardResponse> getCards() {
        return cardService.getCards();
    }

    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }

    @PutMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCard(@PathVariable Long cardId,
                           @RequestBody CardRequest request) {
        cardService.updateCard(cardId, request);
    }

    @GetMapping("/getCards")
    public PageableCardResponse getCards(PageCriteria pageCriteria, CardCriteria cardCriteria) {
        return cardService.getCards(pageCriteria, cardCriteria);
    }
}
