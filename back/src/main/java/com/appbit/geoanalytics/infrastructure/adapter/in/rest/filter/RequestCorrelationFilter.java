package com.appbit.geoanalytics.infrastructure.adapter.in.rest.filter;

import com.appbit.geoanalytics.infrastructure.adapter.in.rest.correlation.RequestCorrelationConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Resolves and propagates the HTTP request correlation id.
 *
 * <p>The filter reads {@code X-Request-Id} when provided by the client. If the
 * header is missing or invalid, it generates a new request id. The resolved id
 * is stored in the servlet request attributes, added to MDC for log
 * correlation, and returned to the client as {@code X-Request-Id}.</p>
 *
 * <p>This class belongs to the REST input adapter and must not be referenced
 * by domain or application code.</p>
 */
@Component
public class RequestCorrelationFilter extends OncePerRequestFilter {

    private static final int MIN_REQUEST_ID_LENGTH = 8;
    private static final int MAX_REQUEST_ID_LENGTH = 64;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestId = resolveRequestId(request);

        request.setAttribute(RequestCorrelationConstants.REQUEST_ID_ATTRIBUTE, requestId);
        response.setHeader(RequestCorrelationConstants.REQUEST_ID_HEADER, requestId);
        MDC.put(RequestCorrelationConstants.REQUEST_ID_MDC_KEY, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(RequestCorrelationConstants.REQUEST_ID_MDC_KEY);
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        String candidate = normalizeNullable(request.getHeader(RequestCorrelationConstants.REQUEST_ID_HEADER));

        if (candidate != null && isValidRequestId(candidate)) {
            return candidate;
        }

        return UUID.randomUUID().toString();
    }

    private boolean isValidRequestId(String value) {
        if (value.length() < MIN_REQUEST_ID_LENGTH || value.length() > MAX_REQUEST_ID_LENGTH) {
            return false;
        }

        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);

            if (isAlphaNumeric(current) || current == '-' || current == '_') {
                continue;
            }

            return false;
        }

        return true;
    }

    private boolean isAlphaNumeric(char value) {
        return (value >= 'A' && value <= 'Z')
                || (value >= 'a' && value <= 'z')
                || (value >= '0' && value <= '9');
    }

    private @Nullable String normalizeNullable(@Nullable String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.strip();
    }
}
