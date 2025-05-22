package org.company.springliquibase.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdFilter extends OncePerRequestFilter {
    private static final String TRACE_ID_PATTERN = "traceId";
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String traceId = MDC.get(TRACE_ID_PATTERN);
            
            if (traceId == null) {
                // If not exists, generate new trace ID
                traceId = UUID.randomUUID().toString();
                MDC.put(TRACE_ID_PATTERN, traceId);
            }
            // Add trace ID to request attributes
            request.setAttribute(TRACE_ID_PATTERN, traceId);
            
            // Add trace ID to response headers
            response.setHeader("X-Trace-ID", traceId);
            
            filterChain.doFilter(request, response);
        } finally {
            // Clean up MDC after request
            MDC.clear();
        }
    }
}
