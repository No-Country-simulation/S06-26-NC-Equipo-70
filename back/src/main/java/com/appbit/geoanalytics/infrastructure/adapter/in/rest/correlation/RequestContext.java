package com.appbit.geoanalytics.infrastructure.adapter.in.rest.correlation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

/**
 * Provides access to HTTP request-scoped infrastructure metadata.
 *
 * <p>This component is used by REST infrastructure classes that need to read
 * the already resolved request correlation id without generating a new one.
 * The request id is normally created by {@code RequestCorrelationFilter} and
 * stored as a servlet request attribute.</p>
 */
@Component
@RequiredArgsConstructor
public class RequestContext {

    /**
     * Returns the current request correlation id.
     *
     * <p>Under normal HTTP execution this value is resolved by the correlation
     * filter. The fallback UUID exists only as a defensive safeguard for
     * unexpected execution paths where an exception handler is invoked without
     * the filter having populated the request attribute.</p>
     *
     * @return non-blank request id
     */
    public String requestId() {
        HttpServletRequest request = currentRequest();

        if (request == null) {
            return UUID.randomUUID().toString();
        }

        Object value = request.getAttribute(RequestCorrelationConstants.REQUEST_ID_ATTRIBUTE);

        if (value instanceof String requestId && !requestId.isBlank()) {
            return requestId.strip();
        }

        return UUID.randomUUID().toString();
    }

    private @Nullable HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }

        return null;
    }
}
