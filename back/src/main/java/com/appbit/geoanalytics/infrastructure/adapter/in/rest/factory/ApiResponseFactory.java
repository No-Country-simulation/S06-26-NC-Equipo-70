package com.appbit.geoanalytics.infrastructure.adapter.in.rest.factory;

import com.appbit.geoanalytics.infrastructure.adapter.in.rest.response.ApiResponse;
import com.appbit.geoanalytics.infrastructure.adapter.in.rest.code.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiResponseFactory {

    private static final String API_VERSION = "v1";

    private final Clock clock;

    public <T> ResponseEntity<ApiResponse<T>> success(
            HttpStatus httpStatus,
            ApiResponseCode code,
            String message,
            @Nullable T data,
            String requestId
    ) {
        ApiResponse<T> body = ApiResponse.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .meta(metadata(requestId))
                .errors(List.of())
                .build();

        return ResponseEntity.status(httpStatus).body(body);
    }

    public ResponseEntity<ApiResponse<Void>> error(
            HttpStatus httpStatus,
            ApiResponseCode code,
            String message,
            List<ApiResponse.ApiErrorDetail> errors,
            String requestId
    ) {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .meta(metadata(requestId))
                .errors(errors)
                .build();

        return ResponseEntity.status(httpStatus).body(body);
    }

    private ApiResponse.Metadata metadata(String requestId) {
        return new ApiResponse.Metadata(
                requestId,
                Instant.now(clock),
                API_VERSION
        );
    }
}
