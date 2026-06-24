package com.appbit.geoanalytics.infrastructure.adapter.in.rest.response;

import com.appbit.geoanalytics.infrastructure.adapter.in.rest.code.ApiResponseCode;
import lombok.Builder;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@Builder
public record ApiResponse<T>(
        boolean success,
        ApiResponseCode code,
        String message,
        @Nullable T data,
        Metadata meta,
        List<ApiErrorDetail> errors
) {

    public ApiResponse {
        if (code == null) {
            throw new IllegalArgumentException("ApiResponseCode cannot be null");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("ApiResponse message cannot be null or blank");
        }

        if (meta == null) {
            throw new IllegalArgumentException("ApiResponse metadata cannot be null");
        }

        errors = errors == null ? List.of() : List.copyOf(errors);
    }

    public record Metadata(
            String requestId,
            Instant timestamp,
            String apiVersion
    ) {

        public Metadata {
            if (requestId == null || requestId.isBlank()) {
                throw new IllegalArgumentException("RequestId cannot be null or blank");
            }

            if (timestamp == null) {
                throw new IllegalArgumentException("Timestamp cannot be null");
            }

            if (apiVersion == null || apiVersion.isBlank()) {
                throw new IllegalArgumentException("ApiVersion cannot be null or blank");
            }

            requestId = requestId.strip();
            apiVersion = apiVersion.strip();
        }
    }

    public record ApiErrorDetail(
            @Nullable String field,
            String reason,
            @Nullable String rejectedValue
    ) {

        public ApiErrorDetail {
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("Error reason cannot be null or blank");
            }

            field = normalizeNullable(field);
            reason = reason.strip();
            rejectedValue = normalizeNullable(rejectedValue);
        }

        private static @Nullable String normalizeNullable(@Nullable String value) {
            if (value == null) {
                return null;
            }

            String trimmed = value.strip();
            return trimmed.isBlank() ? null : trimmed;
        }
    }
}
