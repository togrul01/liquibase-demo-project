package org.company.springliquibase.mapper;

import org.company.springliquibase.entity.CardEntity;
import org.company.springliquibase.entity.UserEntity;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.company.springliquibase.model.response.PageableCardResponse;
import org.company.springliquibase.model.response.PageableUserResponse;
import org.springframework.data.domain.Page;

import static org.company.springliquibase.enums.CardStatus.ACTIVE;

public final class CardMapper {

    private CardMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static CardEntity buildCardEntity(CardRequest request) {
        return CardEntity.builder()
                .cardNumber(request.getCardNumber())
                .cardholderName(request.getCardholderName())
                .expiryDate(request.getExpiryDate())
                .cvv(request.getCvv())
                .cardType(request.getCardType())
                .cardBrand(request.getCardBrand())
                .issueDate(request.getIssueDate())
                .balance(request.getBalance())
                .status(ACTIVE)
                .build();
    }

    public static CardResponse buildCardResponse(CardEntity card) {
        return CardResponse.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cardholderName(card.getCardholderName())
                .expiryDate(card.getExpiryDate())
                .cvv(card.getCvv())
                .cardType(card.getCardType())
                .cardBrand(card.getCardBrand())
                .issueDate(card.getIssueDate())
                .balance(card.getBalance())
                .status(card.getStatus())
                .updatedAt(card.getUpdatedAt())
                .createdAt(card.getCreatedAt())
                .build();
    }

    public static PageableCardResponse mapToPageableCardResponse(Page<CardEntity> cardPage) {
        return PageableCardResponse.builder()
                .cardList(cardPage.getContent().stream()
                        .map(CardMapper::buildCardResponse)
                        .toList())
                .lastPageNumber(cardPage.getTotalPages())
                .totalElements(cardPage.getTotalElements())
                .hasNextPage(cardPage.hasNext())
                .build();
    }
}
