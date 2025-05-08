package org.company.springliquibase.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class CardResponseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        log.info("CardResponseInterceptor: Pre-handle request for {}", request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,  @NonNull HttpServletResponse response,
                           @NonNull Object handler, ModelAndView modelAndView) {
        log.info("CardResponseInterceptor: Post-handle request for {}", request.getRequestURI());
    }

    @Override
    public void afterCompletion( @NonNull HttpServletRequest request,  @NonNull HttpServletResponse response,
                                 @NonNull Object handler, Exception ex) {
        if (ex != null) {
            log.error("CardResponseInterceptor: Error occurred while processing request: {}", ex.getMessage());
        }
        log.info("CardResponseInterceptor: Request completed for {}", request.getRequestURI());
    }
} 