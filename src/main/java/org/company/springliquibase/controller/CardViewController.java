package org.company.springliquibase.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.model.criteria.CardCriteria;
import org.company.springliquibase.model.criteria.PageCriteria;
import org.company.springliquibase.model.request.CardRequest;
import org.company.springliquibase.model.response.CardResponse;
import org.company.springliquibase.model.response.PageableCardResponse;
import org.company.springliquibase.model.response.Response;
import org.company.springliquibase.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CardViewController {
    private final CardService cardService;

    @GetMapping("/card-list.html")
    public String getCardListView(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        try {
            PageCriteria pageCriteria = new PageCriteria(page, size);
            CardCriteria cardCriteria = new CardCriteria();
            PageableCardResponse response = cardService.getCards(pageCriteria, cardCriteria);

            long activeCardsCount = response.getCardList().stream()
                    .filter(card -> "ACTIVE".equals(card.getStatus()))
                    .count();

            BigDecimal totalBalance = response.getCardList().stream()
                    .map(CardResponse::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("cards", response.getCardList());
            model.addAttribute("currentPage", response.getPageNumber());
            model.addAttribute("pageSize", response.getPageSize());
            model.addAttribute("totalElements", response.getTotalElements());
            model.addAttribute("lastPageNumber", response.getLastPageNumber());
            model.addAttribute("activeCardsCount", activeCardsCount);
            model.addAttribute("totalBalance", totalBalance);

            log.info("Retrieved {} cards for page {}", response.getCardList().size(), page);
            return "card-list";
        } catch (Exception e) {
            log.error("Error retrieving cards", e);
            model.addAttribute("error", "Failed to retrieve cards");
            return "error";
        }
    }

    @GetMapping("/card-creator.html")
    public String getCardCreatorView() {
        return "card-creator";
    }

    @PostMapping("/card/create")
    public String createCard(CardRequest cardRequest, RedirectAttributes redirectAttributes) {
        try {
            cardService.createCard(cardRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Card created successfully!");
        } catch (Exception e) {
            log.error("Error creating card: ", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to create card: " + e.getMessage());
        }
        return "redirect:/card-list.html";
    }

    @GetMapping("/v1/cards/{cardId}/edit")
    public String getCardEditView(@PathVariable Long cardId, Model model) {
        try {
            ResponseEntity<Response<CardResponse>> card = cardService.getCard(cardId);
            model.addAttribute("card", card);
            return "card-edit";
        } catch (Exception e) {
            log.error("Error retrieving card for edit: ", e);
            return "redirect:/card-list.html?error=" + e.getMessage();
        }
    }
}
