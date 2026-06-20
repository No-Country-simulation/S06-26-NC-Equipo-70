package com.appbit.geoanalytics.shared.infrastructure.rest.response;

import java.time.Instant;
import java.util.List;

public record ApiResponse<T>(boolean success,
                             String message,
                             T data,
                             List<ApiErrorDetail> errors,
                             MetaData meta) {

    public static <T> ApiResponse<T> success(T data, String requestId) {
        return new ApiResponse<>(
                true,
                "Operación exitosa",
                data,
                List.of(),
                new MetaData(requestId, Instant.now()));
    }

    public static <T> ApiResponse<T> error(String message, List<ApiErrorDetail> errors, String requestId) {
        return new ApiResponse<>(
                false,
                message,
                null,
                errors,
                new MetaData(requestId, Instant.now()));
    }

    public record MetaData(String requestId,
                           Instant timestamp) {
    }

    public record ApiErrorDetail(String code,
                                 String field,
                                 String detail) {
    }
}
