package org.company.springliquibase.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.constants.LogConstants;
import org.company.springliquibase.model.response.Response;
import org.company.springliquibase.enums.Result;
import org.company.springliquibase.dao.CardRepository;
import org.company.springliquibase.entity.CardEntity;
import org.company.springliquibase.enums.CardBrand;
import org.company.springliquibase.enums.CardStatus;
import org.company.springliquibase.enums.CardType;
import org.company.springliquibase.exception.*;
import org.company.springliquibase.logging.Loggable;
import org.company.springliquibase.mapper.CardMapper;
import org.company.springliquibase.model.criteria.CardCriteria;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.company.springliquibase.model.response.PageableCardResponse;
import org.company.springliquibase.specification.CardSpecification;
import org.company.springliquibase.util.LoggingUtil;
import org.company.springliquibase.util.ValidationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {
    @PersistenceContext
    private EntityManager entityManager;

    private final CardRepository cardRepository;
    private final Random random = new Random();
    private static final String ERROR_BUNDLE = "i18n/error";
    private static final String SUCCESS_BUNDLE = "i18n/success";
    private static final String CREATE_CARD_ACTION = "createCard";
    private static final String CARD_NOT_FOUND_KEY = "card.not.found";

    @Override
    @Loggable(action = "createCard")
    @Transactional
    public ResponseEntity<Response<CardResponse>> createCard(CardRequest request) {
        try {
            if (!StringUtils.hasText(request.getCardNumber())) {
                request.setCardNumber(ValidationUtils.
                        generateValidCardNumber(CardBrand.valueOf(request.getCardBrand())));
            }

            request.setCvv(generateRandomCvv());
            LocalDate issueDate = LocalDate.now();
            request.setIssueDate(issueDate);
            request.setExpiryDate(calculateExpiryDate(CardType.valueOf(request.getCardType()), issueDate));

            if (request.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "card.negative.balance");
                log.error(LogConstants.Validation.NEGATIVE_BALANCE, request.getBalance());
                throw new BalanceValidationException(errorMessage);
            }

            validateCardRequest(request);

            CardEntity card = buildCardEntity(request);
            CardEntity savedCard = cardRepository.save(card);
            CardResponse cardResponse = buildCardResponse(savedCard);

            HttpServletRequest currentRequest = ((ServletRequestAttributes) RequestContextHolder.
                    currentRequestAttributes()).getRequest();
            currentRequest.setAttribute("cardResponse", cardResponse);

            String successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "card.create.success");
            log.info(successMessage);
            LoggingUtil.logApiResponse(CREATE_CARD_ACTION, cardResponse);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response<>(Result.CARD_CREATED.getCode(), successMessage, cardResponse));

        } catch (IllegalArgumentException e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "card.invalid.type");
            log.error(LogConstants.Validation.INVALID_CARD_TYPE, request.getCardType());
            throw new CardTypeValidationException(errorMessage);
        } catch (BalanceValidationException | CardTypeValidationException e) {
            log.error(LogConstants.Validation.VALIDATION_FAILED, e.getMessage());
            throw e;
        } catch (Exception e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "card.create.error");
            log.error(LogConstants.Error.CREATING_CARD, e.getMessage());
            throw new CardCreationException(errorMessage);
        }
    }

    @Override
    @Loggable(action = "getCardById")
    public ResponseEntity<Response<CardResponse>> getCard(Long cardId) {
        log.info(LogConstants.Action.GET_CARD_BY_ID_START, cardId);
        try {
            var card = getCardById(cardId);
            var cardResponse = buildCardResponseForCard(card);
            LoggingUtil.logApiResponse(CREATE_CARD_ACTION, cardResponse);

            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "card.get.success");
            log.info(successMessage);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response<>(Result.CARD_FOUND.getCode(), successMessage, cardResponse));
        } catch (CardNotFoundException e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, CARD_NOT_FOUND_KEY);
            log.error(LogConstants.Error.CARD_NOT_FOUND_WITH_ID, cardId);
            throw new CardNotFoundException(errorMessage);
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "card.get.error");
            log.error(LogConstants.Error.GETTING_CARD, e.getMessage());
            throw new CardNotFoundException(errorMessage);
        }
    }

    @Override
    @Loggable(action = "getCardByNumber")
    public ResponseEntity<Response<CardResponse>> getCard(String cardNumber) {
        log.info(LogConstants.Action.GET_CARD_START, maskCardNumber(cardNumber));
        try {
            var card = fetchCardIfExist(cardNumber);
            var cardResponse = buildCardResponse(card);
            LoggingUtil.logApiResponse(CREATE_CARD_ACTION, cardResponse);

            var successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "card.get.success");
            log.info(successMessage);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response<>(Result.CARD_FOUND.getCode(), successMessage, cardResponse));
        } catch (CardNotFoundException e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, CARD_NOT_FOUND_KEY);
            log.error(LogConstants.Error.CARD_NOT_FOUND_WITH_NUMBER, maskCardNumber(cardNumber));
            throw new CardNotFoundException(errorMessage);
        } catch (Exception e) {
            var errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "card.get.error");
            log.error(LogConstants.Error.GETTING_CARD, e.getMessage());
            throw new CardNotFoundException(errorMessage);
        }
    }

    public PageableCardResponse getCards(PageCriteria pageCriteria, CardCriteria cardCriteria) {
        log.info("ActionLog.getUsers.start criteria: {}, userCriteria: {}", pageCriteria, cardCriteria);
        var pageNumber = pageCriteria.getPage() == null ? PAGE_DEFAULT_VALUE.getIntegerValue() : pageCriteria.getPage();
        var count = pageCriteria.getCount() == null ? COUNT_DEFAULT_VALUE.getIntegerValue() : pageCriteria.getCount();
        var cardPage = cardRepository.findAll(new CardSpecification(cardCriteria), PageRequest.of(pageNumber, count));
        log.info("ActionLog.getUsers.end criteria: {}, userCriteria: {}", pageCriteria, cardCriteria);
        return mapToPageableCardResponse(cardPage);
    }

    @Override
    @Loggable(action = "findAll")
    public ResponseEntity<Response<PageableCardResponse>> findAll(String name, int page, int size) {
        log.info("ActionLog.findAll.start with name: {}", name);
        try {
            Specification<CardEntity> spec = Specification.where(CardSpecification.active());

            if (StringUtils.hasText(name)) {
                spec = spec.and((root, query, cb) ->
                        cb.like(cb.lower(root.get("cardholderName")), "%" + name.toLowerCase() + "%"));
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<CardEntity> cardPage = cardRepository.findAll(spec, pageable);

            PageableCardResponse response = new PageableCardResponse();
            response.setTotalElements(cardPage.getTotalElements());
            response.setHasNextPage(cardPage.hasNext());
            response.setLastPageNumber(cardPage.getTotalPages());
            response.setCardList(cardPage.getContent().stream().map(CardMapper::buildCardResponse).toList());

            String successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "card.find.all.success");
            log.info(successMessage);

            return ResponseEntity.ok(new Response<>(Result.CARD_FOUND.getCode(), successMessage, response));
        } catch (Exception e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "card.find.all.error");
            log.error(LogConstants.Error.FINDING_CARDS, e.getMessage(), e);
            
            // Create an empty PageableCardResponse for error case
            PageableCardResponse errorResponse = new PageableCardResponse();
            errorResponse.setTotalElements(0L);
            errorResponse.setHasNextPage(false);
            errorResponse.setLastPageNumber(0);
            errorResponse.setCardList(List.of());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(Result.ERROR.getCode(), errorMessage, errorResponse));
        }
    }

    @Override
    @Loggable(action = "deleteCard")
    public ResponseEntity<Response<String>> deleteCard(Long cardId) {
        log.info(LogConstants.Action.DELETE_CARD_START, cardId);
        try {
            CardEntity card = fetchCardIfExist(cardId);
            card.setStatus(DELETED);
            cardRepository.save(card);

            String successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "card.delete.success");
            log.info(successMessage);
            return ResponseEntity.ok(new Response<>(Result.CARD_DELETED.getCode(), successMessage, "Card deleted successfully"));
        } catch (CardNotFoundException e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, CARD_NOT_FOUND_KEY);
            log.error(LogConstants.Error.CARD_NOT_FOUND_WITH_ID, cardId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response<>(Result.CARD_NOT_FOUND.getCode(), errorMessage, 
                        "Card not found with id: " + cardId));
        } catch (Exception e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "card.delete.error");
            log.error(LogConstants.Error.DELETING_CARD, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(Result.CARD_DELETE_ERROR.getCode(), errorMessage,
                        "Error deleting card: " + e.getMessage()));
        }
    }

    @Override
    @Loggable(action = "updateCard")
    @Transactional
    public void updateCard(Long cardId, CardRequest request) {
        log.info(LogConstants.Action.UPDATE_CARD_START, cardId);
        try {
            CardEntity card = fetchCardIfExist(cardId);
            card.setStatus(IN_PROGRESS);

            Optional.ofNullable(request.getCardholderName()).ifPresent(card::setCardholderName);
            Optional.ofNullable(request.getExpiryDate()).ifPresent(card::setExpiryDate);
            Optional.ofNullable(request.getCvv()).ifPresent(card::setCvv);
            Optional.ofNullable(request.getCardType()).ifPresent(card::setCardType);
            Optional.ofNullable(request.getIssueDate()).ifPresent(card::setIssueDate);
            Optional.ofNullable(request.getBalance()).ifPresent(card::setBalance);

            cardRepository.save(card);

            String successMessage = getLocalizedMessageByKey(SUCCESS_BUNDLE, "card.update.success");
            log.info(successMessage);
        } catch (CardNotFoundException e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, CARD_NOT_FOUND_KEY);
            log.error(LogConstants.Error.CARD_NOT_FOUND_WITH_ID, cardId);
            throw new CardNotFoundException(errorMessage);
        } catch (Exception e) {
            String errorMessage = getLocalizedMessageByKey(ERROR_BUNDLE, "card.update.error");
            log.error(LogConstants.Error.UPDATING_CARD, e.getMessage());
            throw new CardNotFoundException(errorMessage);
        }
    }

    @Transactional
    @Loggable(action = "increaseCardBalances")
    @Override
    public void increaseCardBalances() {
        cardRepository.increaseActiveCardBalances();
    }

    @Override
    @Transactional
    @Loggable(action = "increaseCardBalancesWithJpa")
    public void increaseCardBalancesWithJpa() {
        int batchSize = 50;
        List<CardEntity> cards = cardRepository.findAllByStatusNot(CardStatus.DELETED);

        for (int i = 0; i < cards.size(); i++) {
            CardEntity card = cards.get(i);
            try {
                BigDecimal newBalance = card.getBalance().multiply(new BigDecimal("1.05"));
                if (newBalance.compareTo(new BigDecimal("9999999999999.99")) > 0) {
                    log.warn(LogConstants.Balance.EXCEED_MAX, maskCardNumber(card.getCardNumber()));
                    continue;
                }
                card.setBalance(newBalance);

                if (i > 0 && i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            } catch (Exception e) {
                log.error(LogConstants.Error.UPDATING_BALANCE, maskCardNumber(card.getCardNumber()), e.getMessage());
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

    private CardEntity getCardById(Long cardId) {
        return fetchCardIfExist(cardId);
    }

    private CardResponse buildCardResponseForCard(CardEntity card) {
        return buildCardResponse(card);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber != null && cardNumber.length() > 8) {
            return "****" + cardNumber.substring(cardNumber.length() - 8);
        } else if (cardNumber != null) {
            return "****" + cardNumber;
        }
        return "****";
    }

    private PageableCardResponse mapToPageableCardResponse(org.springframework.data.domain.Page<CardEntity> cardPage) {
        return PageableCardResponse.builder()
                .cardList(cardPage.getContent().stream()
                        .map(CardMapper::buildCardResponse)
                        .toList())
                .pageNumber(cardPage.getNumber())
                .pageSize(cardPage.getSize())
                .totalElements(cardPage.getTotalElements())
                .lastPageNumber(cardPage.getTotalPages())
                .hasNextPage(cardPage.hasNext())
                .build();
    }

    private CardEntity fetchCardIfExist(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber).orElseThrow(() -> {
            log.error(LogConstants.Error.CARD_NOT_FOUND_WITH_NUMBER, maskCardNumber(cardNumber));
            var message = getLocalizedMessageByKey(ERROR_BUNDLE, CARD_NOT_FOUND_KEY);
            return new CardNotFoundException(message);
        });
    }

    private CardEntity fetchCardIfExist(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() ->
        {
            log.error(LogConstants.Error.CARD_NOT_FOUND_WITH_ID, cardId);
            return new CardNotFoundException(
                    getLocalizedMessageByKey(ERROR_BUNDLE, CARD_NOT_FOUND_KEY));
        });
    }
}

