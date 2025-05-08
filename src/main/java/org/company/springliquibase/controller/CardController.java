package org.company.springliquibase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/v1/cards")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Card Management", description = "APIs for managing credit/debit cards")
public class CardController {
    private final CardService cardService;

    @Operation(summary = "Create a new card", description = "Creates a new credit or debit card with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Card created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest cardRequest) {
        CardResponse createdCard = cardService.createCard(cardRequest);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @Operation(summary = "Get card by ID", description = "Retrieves a card's details by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card found"),
        @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @GetMapping("/{cardId}")
    public CardResponse getCard(@Parameter(description = "ID of the card to retrieve") @PathVariable Long cardId) {
        return cardService.getCard(cardId);
    }

    @Operation(summary = "Get all cards", description = "Retrieves a list of all cards in the system")
    @GetMapping
    public List<CardResponse> getCards() {
        return cardService.getCards();
    }

    @Operation(summary = "Delete a card", description = "Deletes a card by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@Parameter(description = "ID of the card to delete") @PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }

    @Operation(summary = "Update a card", description = "Updates an existing card's details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Card updated successfully"),
        @ApiResponse(responseCode = "404", description = "Card not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCard(
            @Parameter(description = "ID of the card to update") @PathVariable Long cardId,
            @RequestBody CardRequest request) {
        cardService.updateCard(cardId, request);
    }

    @Operation(summary = "Get paginated cards", description = "Retrieves a paginated list of cards with optional filtering")
    @GetMapping("/getCards")
    public PageableCardResponse getCards(
            @Parameter(description = "Pagination criteria") PageCriteria pageCriteria,
            @Parameter(description = "Filter criteria") CardCriteria cardCriteria) {
        return cardService.getCards(pageCriteria, cardCriteria);
    }
}