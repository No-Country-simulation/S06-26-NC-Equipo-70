package com.appbit.geoanalytics.infrastructure.adapter.in.rest.correlation;

/**
 * Shared constants used by REST request correlation infrastructure.
 *
 * <p>This class belongs to the HTTP input adapter. It defines the public
 * request id header, the internal servlet request attribute, and the MDC key
 * used for log correlation.</p>
 */
public final class RequestCorrelationConstants {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String REQUEST_ID_ATTRIBUTE = "appbit.requestId";
    public static final String REQUEST_ID_MDC_KEY = "requestId";

    private RequestCorrelationConstants() {
        throw new AssertionError("RequestCorrelationConstants cannot be instantiated");
    }
}
