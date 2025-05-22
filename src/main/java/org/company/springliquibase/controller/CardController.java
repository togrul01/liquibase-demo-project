package org.company.springliquibase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.model.response.Response;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.company.springliquibase.model.response.PageableCardResponse;
import org.company.springliquibase.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Response<CardResponse>> createCard(@RequestBody CardRequest cardRequest) {
        return cardService.createCard(cardRequest);
    }

    @Operation(summary = "Get card by ID", description = "Retrieves a card's details by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card found"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @GetMapping("/{cardId}")
    public ResponseEntity<Response<CardResponse>> getCard(@PathVariable Long cardId) {
        return cardService.getCard(cardId);
    }

    @Operation(summary = "Get card by card number", description = "Retrieves a card's details by its card number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card found"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @GetMapping("/number/{cardNumber}")
    public ResponseEntity<Response<CardResponse>> getCardByNumber(@PathVariable String cardNumber) {
        return cardService.getCard(cardNumber);
    }

    @Operation(summary = "Find cards", description = "Finds cards with optional filtering by cardholder name. " +
            "If no name is provided, returns all active cards.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Response<PageableCardResponse>> findAll(@RequestParam(required = false) String name,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size) {
        return cardService.findAll(name, page, size);
    }

    @Operation(summary = "Delete a card", description = "Deletes a card by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Response<String>> deleteCard(@PathVariable Long cardId) {
        return cardService.deleteCard(cardId);
    }

}