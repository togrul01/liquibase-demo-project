package org.company.springliquibase.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.company.springliquibase.model.response.CardResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    // Constant string for the masked value.
    private static final String MASKED_VALUE = "******";

    /**
     * Called before the request is handled.
     *
     * @param request  The incoming HttpServletRequest object.
     * @param response The outgoing HttpServletResponse object.
     * @param handler  The handler object to be processed.
     * @return A boolean indicating whether the request should be processed further.
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response, @NonNull Object handler) {
        long startTime = System.currentTimeMillis();

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // Retrieve the CardResponse stored in the request
        CardResponse cardResponse = (CardResponse) request.getAttribute("cardResponse");

        if (cardResponse != null) {
            log.info("Request started - Method: {}, URI: {}, Masked CardNumber: {}, Card Response: {}",
                    method, uri, maskSensitiveData(cardResponse.getCardNumber()), cardResponse);
        } else {
            log.info("Request started - Method: {}, URI: {}, No card created", method, uri);
        }

        request.setAttribute("startTime", startTime);
        return true;
    }


    /**
     * Called after the request is handled.
     *
     * @param request  The incoming HttpServletRequest object.
     * @param response The outgoing HttpServletResponse object.
     * @param handler  The handler object that was processed.
     * @param ex       The exception that was thrown (if any).
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        long startTime = (long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;

        String uri = request.getRequestURI();
        String method = request.getMethod();
        int status = response.getStatus();

        if (ex != null) {
            log.error("Request finished with error - Method: {}, URI: {}, Status: {}, Error: {}",
                    method, uri, status, ex.getMessage());
        } else {
            log.info("Request finished - Method: {}, URI: {}, Status: {}, Duration: {} ms",
                    method, uri, status, duration);
        }
    }

    /**
     * Masks sensitive data like the card number.
     *
     * @param value The value to be masked.
     * @return The masked value.
     */
    private String maskSensitiveData(String value) {
        if (value != null && value.length() > 4) {
            int visibleDigits = 4;
            int maskedLength = value.length() - visibleDigits;
            return "*".repeat(maskedLength) +
                    value.substring(maskedLength);
        } else if (value != null) {
            return "****"; // Handle short card numbers
        }
        return MASKED_VALUE;
    }
}
