package org.company.springliquibase.util;

import org.company.springliquibase.enums.CardBrand;
import org.company.springliquibase.enums.CardType;
import org.company.springliquibase.constants.ExceptionConstants;
import org.company.springliquibase.exception.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.security.SecureRandom;

public class ValidationUtils {
    private static final SecureRandom RANDOM = new SecureRandom();

    private ValidationUtils() {
    }

    public static void validateNotEmpty(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new CardholderNameValidationException(errorMessage);
        }
    }

    public static void validatePositive(BigDecimal value, String errorMessage) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceValidationException(errorMessage);
        }
    }

    public static void validateFutureDate(LocalDate date, String errorMessage) {
        if (date != null && date.isBefore(LocalDate.now())) {
            throw new ExpiryDateValidationException(errorMessage);
        }
    }

    public static void validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new CardNumberValidationException(ExceptionConstants.EMPTY_CARD_NUMBER.getMessage());
        }
        if (!isValidCardNumber(cardNumber)) {
            throw new CardNumberValidationException(ExceptionConstants.INVALID_CARD_NUMBER.getMessage());
        }
    }


    public static void validateCvv(String cvv, String cardType) {
        if (cvv == null || cvv.trim().isEmpty()) {
            throw new CvvValidationException(ExceptionConstants.EMPTY_CVV.getMessage());
        }
        if (!isValidCvv(cvv, cardType)) {
            throw new CvvValidationException("Invalid CVV number.");
        }
    }

    public static String generateValidCardNumber(CardBrand cardBrand) {
        StringBuilder cardNumber = new StringBuilder();
        switch (cardBrand) {
            case VISA -> cardNumber.append("4");       // Visa prefix
            case MASTERCARD -> cardNumber.append(51 + RANDOM.nextInt(5)); // MasterCard prefix
            case AMEX -> cardNumber.append("34");      // AMEX prefix
            case DISCOVER -> cardNumber.append("6011");    // Discover prefix
            default -> throw new IllegalArgumentException("Invalid card type: " + cardBrand);
        }

        // Ensure that we fill the card number to a total length of 15 before adding the check digit
        while (cardNumber.length() < 15) {
            cardNumber.append(RANDOM.nextInt(10)); // Append random digits
        }

        int checkDigit = generateCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit); // Append the check digit

        return cardNumber.toString(); // Return the complete card number (16 digits)
    }

    private static int generateCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }

    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            return false;
        }
        return generateCheckDigit(cardNumber.substring(0, 15)) == Character.getNumericValue(cardNumber.charAt(15));
    }

    private static boolean isValidCvv(String cvv, String cardType) {
        if (!cvv.matches("\\d+")) {
            return false;
        }
        if (cardType.equalsIgnoreCase("AMEX")) {
            return cvv.length() == 4;
        }
        return cvv.length() == 3;
    }

    public static LocalDate calculateExpiryDate(CardType cardType, LocalDate issueDate) {
        LocalDate expiryDate = switch (cardType) {
            case CREDIT -> issueDate.plusYears(5);
            case DEBIT -> issueDate.plusYears(3);
        };

        int maxDay = expiryDate.lengthOfMonth();
        int randomDay = RANDOM.nextInt(1, maxDay + 1);
        return expiryDate.withDayOfMonth(randomDay);
    }
}




