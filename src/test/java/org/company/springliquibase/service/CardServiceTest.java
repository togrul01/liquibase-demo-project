package org.company.springliquibase.service;

import org.company.springliquibase.exception.CardNotFoundException;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Test
    void whenCreateCard_thenCardShouldBeCreated() {
        // Given
        CardRequest request = new CardRequest();
        request.setCardNumber("4111111111111111");
        request.setCardholderName("John Doe");
        request.setExpiryDate(LocalDate.now().plusYears(1));
        request.setCvv("123");
        request.setCardType("CREDIT");
        request.setCardBrand("VISA");
        request.setIssueDate(LocalDate.now());
        request.setBalance(new BigDecimal("1000.00"));

        // When
        CardResponse response = cardService.createCard(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("4111111111111111", response.getCardNumber());
        assertEquals("John Doe", response.getCardholderName());
    }

    @Test
    void whenGetNonExistentCard_thenThrowException() {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        assertThrows(CardNotFoundException.class, () -> {
            cardService.getCard(nonExistentId);
        });
    }

    @Test
    void whenCreateAndGetCard_thenCardShouldBeRetrieved() {
        // Given
        CardRequest request = new CardRequest();
        request.setCardNumber("4111111111111111");
        request.setCardholderName("John Doe");
        request.setExpiryDate(LocalDate.now().plusYears(1));
        request.setCvv("123");
        request.setCardType("CREDIT");
        request.setCardBrand("VISA");
        request.setIssueDate(LocalDate.now());
        request.setBalance(new BigDecimal("1000.00"));

        // When
        CardResponse createdCard = cardService.createCard(request);
        CardResponse retrievedCard = cardService.getCard(createdCard.getId());

        // Then
        assertNotNull(retrievedCard);
        assertEquals(createdCard.getId(), retrievedCard.getId());
        assertEquals(createdCard.getCardNumber(), retrievedCard.getCardNumber());
        assertEquals(createdCard.getCardholderName(), retrievedCard.getCardholderName());
    }
} 