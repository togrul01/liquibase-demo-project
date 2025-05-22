package org.company.springliquibase.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.model.response.CardResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response, @NonNull Object handler) {
        long startTime = System.currentTimeMillis();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.info("Request started - Method: {}, URI: {}", method, uri);
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        long startTime = (long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();

        CardResponse cardResponse = (CardResponse) request.getAttribute("cardResponse");

        if (ex != null) {
            log.error("Request finished with error - Method: {}, URI: {}, Status: {}, Error: {}",
                    method, uri, status, ex.getMessage());
        } else {
            if (cardResponse != null) {
                try {
                    // Mask the card number before logging
                    String originalCardNumber = cardResponse.getCardNumber();
                    cardResponse.setCardNumber(maskSensitiveData(originalCardNumber));

                    log.info("Card operation completed successfully: {}", cardResponse);

                    // Configure ObjectMapper for pretty printing and proper date format
                    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                    String cardResponseJson = objectMapper.writeValueAsString(cardResponse);
                    log.info("Card Response JSON: {}", cardResponseJson);

                    // Restore original card number
                    cardResponse.setCardNumber(originalCardNumber);
                } catch (Exception e) {
                    log.error("Error serializing CardResponse", e);
                    log.info("Request finished - Method: {}, URI: {}, Status: {}, Duration: {} ms, Card Response: {}",
                            method, uri, status, duration, cardResponse);
                }
            } else {
                log.info("Request finished - Method: {}, URI: {}, Status: {}, Duration: {} ms",
                        method, uri, status, duration);
            }
        }
    }

    private String maskSensitiveData(String value) {
        if (value != null && value.length() > 4) {
            int length = value.length();
            return "********" + value.substring(length - 4);
        }
        return value;
    }
}