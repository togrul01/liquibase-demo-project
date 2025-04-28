package org.company.springliquibase.model.response;

import lombok.*;
import org.company.springliquibase.enums.CardStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponse {
    private Long id;
    private String cardNumber;
    private String cardholderName;
    private LocalDate expiryDate;
    private String cvv;
    private String cardType;
    private String cardBrand;
    private LocalDate issueDate;
    private BigDecimal balance;
    private CardStatus status;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
