package org.company.springliquibase.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.dao.CardRepository;
import org.company.springliquibase.entity.CardEntity;
import org.company.springliquibase.enums.CardBrand;
import org.company.springliquibase.enums.CardType;
import org.company.springliquibase.exception.*;
import org.company.springliquibase.mapper.CardMapper;
import org.company.springliquibase.model.criteria.CardCriteria;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.company.springliquibase.model.response.PageableCardResponse;
import org.company.springliquibase.specification.CardSpecification;
import org.company.springliquibase.util.ValidationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.company.springliquibase.constants.CriteriaConstants.COUNT_DEFAULT_VALUE;
import static org.company.springliquibase.constants.CriteriaConstants.PAGE_DEFAULT_VALUE;
import static org.company.springliquibase.enums.CardStatus.*;
import static org.company.springliquibase.mapper.CardMapper.*;
import static org.company.springliquibase.util.LocalizationUtil.getLocalizedMessageByKey;
import static org.company.springliquibase.util.ValidationUtils.calculateExpiryDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {
    @PersistenceContext
    private EntityManager entityManager;

    private final CardRepository cardRepository;
    private final Random random = new Random(); //Creating a single Random object at class level
    private static final String ERROR_BUNDLE = "i18n/error"; // Constant for the base string

    @Override
    @Transactional
    public void createCard(CardRequest request) {
        try {
            log.info("ActionLog.createCard.start create card");
            if (request.getCardNumber() == null || request.getCardNumber().trim().isEmpty()) {
                request.setCardNumber(ValidationUtils.generateValidCardNumber(CardBrand.valueOf(request.getCardBrand())));
            }

            request.setCvv(generateRandomCvv());
            LocalDate issueDate = LocalDate.now();
            request.setIssueDate(issueDate);
            request.setExpiryDate(calculateExpiryDate(CardType.valueOf(request.getCardType()), issueDate));

            if (request.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "negative.balance.exception");
                log.error("Negative balance provided: {}", request.getBalance());
                throw new BalanceValidationException(errorMessage);
            }

            validateCardRequest(request);
            cardRepository.save(buildCardEntity(request));

            log.info("ActionLog.createCard.success create card");

        } catch (IllegalArgumentException e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "invalid.card.type.exception");
            log.error("Invalid card type provided: {}", request.getCardType());
            throw new CardTypeValidationException(errorMessage);
        } catch (BalanceValidationException e) {
            log.error("Negative balance detected: {}", e.getMessage());
            throw e;
        } catch (CardTypeValidationException e) {
            log.error("Card type validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "unexpected.error.exception");
            log.error("Unexpected error occurred while creating card", e);
            throw new RuntimeException(errorMessage);
        }
    }


    @Override
    public CardResponse getCard(Long cardId) {
        log.info("ActionLog.getCard.start with id: {}", cardId);
        CardEntity card = fetchCardIfExist(cardId);
        log.info("ActionLog.getCard.success with id: {}", cardId);
        return buildCardResponse(card);
    }

    @Override
    public CardResponse getCard(String cardNumber) {
        log.info("ActionLog.getCard.start with card number: {}", cardNumber);
        CardEntity card = fetchCardIfExist(cardNumber);
        log.info("ActionLog.getCard.success with card number: {}", cardNumber);
        return buildCardResponse(card);
    }

    public PageableCardResponse getCards(PageCriteria pageCriteria, CardCriteria cardCriteria) {
        log.info("ActionLog.getUsers.start criteria: {}, userCriteria: {}", pageCriteria, cardCriteria);
        var pageNumber = pageCriteria.getPage() == null ? PAGE_DEFAULT_VALUE.getIntegerValue() : pageCriteria.getPage();
        var count = pageCriteria.getCount() == null ? COUNT_DEFAULT_VALUE.getIntegerValue() : pageCriteria.getCount();
        var cardPage = cardRepository.findAll(new CardSpecification(cardCriteria), PageRequest.of(pageNumber, count));
        log.info("ActionLog.getUsers.end criteria: {}, userCriteria: {}", pageCriteria, cardCriteria);
        return mapToPageableCardResponse(cardPage);
    }

    private CardEntity fetchCardIfExist(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> {
            log.error("ActionLog.fetchCardIfExist.start with card number: {} not found", cardNumber);
            var message = getLocalizedMessageByKey(ERROR_BUNDLE, "card.not.found.exception");
            return new CardNotFoundException(message);
        });
    }


    @Override
    public List<CardResponse> getCards() {
        log.info("ActionLog.getCards.start fetching all cards");
        List<CardEntity> cards = cardRepository.findAll();
        return cards.stream()
                .map(CardMapper::buildCardResponse)
                .toList();
    }

    @Override
    public void deleteCard(Long cardId) {
        log.info("ActionLog.deleteCard.start with id: {}", cardId);
        CardEntity card = fetchCardIfExist(cardId);
        card.setStatus(DELETED);
        log.info("ActionLog.deleteCard.success with id: {}", cardId);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void updateCard(Long cardId, CardRequest request) {
        log.info("ActionLog.updateCard.start with id: {}", cardId);
        CardEntity card = fetchCardIfExist(cardId);
        card.setStatus(IN_PROGRESS);

        Optional.ofNullable(request.getCardholderName()).ifPresent(card::setCardholderName);
        Optional.ofNullable(request.getExpiryDate()).ifPresent(card::setExpiryDate);
        Optional.ofNullable(request.getCvv()).ifPresent(card::setCvv);
        Optional.ofNullable(request.getCardType()).ifPresent(card::setCardType);
        Optional.ofNullable(request.getIssueDate()).ifPresent(card::setIssueDate);
        Optional.ofNullable(request.getBalance()).ifPresent(card::setBalance);

        cardRepository.save(card);

        log.info("ActionLog.updateCard.success with id: {}", cardId);
    }

    private CardEntity fetchCardIfExist(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() ->
        {
            log.error("ActionLog.fetchCardIfExist.start with id: {} not found", cardId);
            return new CardNotFoundException(
                    getLocalizedMessageByKey(ERROR_BUNDLE, "card.not.found.exception"));
        });
    }

    @Transactional
    @Override
    public void increaseCardBalances() {
        cardRepository.increaseActiveCardBalances();
    }

    @Override
    @Transactional
    public void increaseCardBalancesWithJpa() {
        int batchSize = 50;
        List<CardEntity> cards = cardRepository.findAll();

        for (int i = 0; i < cards.size(); i++) {
            CardEntity card = cards.get(i);
            BigDecimal percentage = new BigDecimal("0.05");
            BigDecimal currentBalance = card.getBalance();
            BigDecimal increaseAmount = currentBalance.multiply(percentage);
            card.setBalance(currentBalance.add(increaseAmount));

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush(); // Deyişiklikleri database gönder
                entityManager.clear(); // memorydeki  obyektleri temizleyir
            }
        }
        entityManager.flush();
    }


    private void validateCardRequest(CardRequest request) {
        ValidationUtils.validateNotEmpty(request.getCardNumber(),
                getLocalizedMessageByKey(ERROR_BUNDLE, "empty.card.number.exception"));
        ValidationUtils.validateCardNumber(request.getCardNumber());

        ValidationUtils.validateNotEmpty(request.getCardholderName(),
                getLocalizedMessageByKey(ERROR_BUNDLE, "empty.cardholder.name.exception"));

        ValidationUtils.validateNotEmpty(request.getCvv(),
                getLocalizedMessageByKey(ERROR_BUNDLE, "empty.cvv.exception"));

        ValidationUtils.validateCvv(request.getCvv(), request.getCardType());

        ValidationUtils.validateNotEmpty(request.getCardType(),
                getLocalizedMessageByKey(ERROR_BUNDLE, "empty.card.type.exception"));

        List<String> validCardTypes = List.of("DEBIT", "CREDIT");
        if (!validCardTypes.contains(request.getCardType().toUpperCase())) {
            throw new CardTypeValidationException(
                    getLocalizedMessageByKey(ERROR_BUNDLE, "invalid.card.type.exception"));
        }

        ValidationUtils.validateFutureDate(request.getExpiryDate(),
                getLocalizedMessageByKey(ERROR_BUNDLE, "expired.date.exception"));

        ValidationUtils.validatePositive(request.getBalance(),
                getLocalizedMessageByKey(ERROR_BUNDLE, "negative.balance.exception"));
        if (cardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new DuplicateCardNumberException(
                    getLocalizedMessageByKey(ERROR_BUNDLE, "duplicate.card.number.exception"));
        }
    }

    private String generateRandomCvv() {
        int cvv = 100 + random.nextInt(900);
        return String.valueOf(cvv);
    }

}
