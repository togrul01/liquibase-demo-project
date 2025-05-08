package org.company.springliquibase.entity;

import jakarta.persistence.*;
import lombok.*;
import org.company.springliquibase.enums.CardStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;

    private String cardholderName;

    private LocalDate expiryDate;

    private String cvv;

    private String cardType;

    private String cardBrand;

    private LocalDate issueDate;

    @Column(precision = 15, scale = 2)
    private BigDecimal balance;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(STRING)
    private CardStatus status;

}
