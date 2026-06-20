package com.appbit.geoanalytics.shared.infrastructure.advice;

import com.appbit.geoanalytics.shared.infrastructure.rest.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Global exception handler that intercepts application-wide exceptions
 * and maps them into a standardized {@link ApiResponse} structure.
 * @author NC-Equipo-70
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles Jakarta validation constraints triggered by {@code @Valid} on request bodies.
     *
     * @param ex the payload validation exception
     * @return a {@link ResponseEntity} containing structured field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String requestId = generateRequestId();
        log.warn("Validation failed for request [{}]: {}", requestId, ex.getMessage());

        List<ApiResponse.ApiErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiResponse.ApiErrorDetail(
                        "INVALID_INPUT",
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ApiResponse<Void> response = ApiResponse.error("The request could not be processed due to validation errors.", errors, requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles Jakarta constraint violations on inline parameters (PathVariables and RequestParams).
     *
     * @param ex the constraint violation exception
     * @return a {@link ResponseEntity} containing constraint breakdown
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String requestId = generateRequestId();
        log.warn("Constraint violation detected [{}]: {}", requestId, ex.getMessage());

        List<ApiResponse.ApiErrorDetail> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ApiResponse.ApiErrorDetail(
                        "CONSTRAINT_VIOLATION",
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .toList();

        ApiResponse<Void> response = ApiResponse.error("Validation constraints were violated.", errors, requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles 404 Not Found errors when an API route or a static resource does not exist.
     *
     * @param ex the resource didn't find an exception
     * @return a {@link ResponseEntity} indicating the path is missing
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(NoResourceFoundException ex) {
        String requestId = generateRequestId();
        log.warn("Resource or route not found [{}]: URL path='{}'", requestId, ex.getResourcePath());

        ApiResponse.ApiErrorDetail errorDetail = new ApiResponse.ApiErrorDetail(
                "RESOURCE_NOT_FOUND",
                "path",
                String.format("The requested endpoint or resource '%s' does not exist.", ex.getResourcePath().isBlank() ? "/" : ex.getResourcePath())
        );

        ApiResponse<Void> response = ApiResponse.error("The requested resource could not be found.", List.of(errorDetail), requestId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles malformed payloads or cases where the HTTP body is missing or unreadable.
     *
     * @param ex the unreadable message exception
     * @return a {@link ResponseEntity} indicating bad request formatting
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String requestId = generateRequestId();
        log.warn("Malformed HTTP request body [{}]: {}", requestId, ex.getMessage());

        ApiResponse.ApiErrorDetail errorDetail = new ApiResponse.ApiErrorDetail(
                "MALFORMED_JSON",
                "body",
                "The provided JSON body is invalid, empty, or cannot be deserialized."
        );

        ApiResponse<Void> response = ApiResponse.error("Request body is missing or malformed.", List.of(errorDetail), requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles argument type conversion failures in HTTP parameters or URI paths.
     *
     * @param ex the type mismatch exception
     * @return a {@link ResponseEntity} indicating parameter mismatch
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String requestId = generateRequestId();
        log.warn("Type mismatch error [{}]: {}", requestId, ex.getMessage());

        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        ApiResponse.ApiErrorDetail errorDetail = new ApiResponse.ApiErrorDetail(
                "TYPE_MISMATCH",
                ex.getName(),
                String.format("Expected data type '%s' but received value '%s'.", requiredType, ex.getValue())
        );

        ApiResponse<Void> response = ApiResponse.error("Invalid parameter type provided.", List.of(errorDetail), requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles requests targeting an unsupported HTTP method on a valid endpoint.
     *
     * @param ex the method didn't support exception
     * @return a {@link ResponseEntity} with method not allowed status
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String requestId = generateRequestId();
        log.warn("HTTP method not allowed [{}]: method={}", requestId, ex.getMethod());

        ApiResponse.ApiErrorDetail errorDetail = new ApiResponse.ApiErrorDetail(
                "METHOD_NOT_ALLOWED",
                "http_method",
                String.format("HTTP method '%s' is not supported for this route.", ex.getMethod())
        );

        ApiResponse<Void> response = ApiResponse.error("HTTP method not allowed.", List.of(errorDetail), requestId);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * Handles requests delivering an invalid or unsupported Content-Type header.
     *
     * @param ex the media type exception
     * @return a {@link ResponseEntity} with unsupported media type status
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String requestId = generateRequestId();
        log.warn("HTTP media type unsupported [{}]: type={}", requestId, ex.getContentType());

        ApiResponse.ApiErrorDetail errorDetail = new ApiResponse.ApiErrorDetail(
                "UNSUPPORTED_MEDIA_TYPE",
                "Content-Type",
                String.format("Media type '%s' is not supported by the system.", ex.getContentType())
        );

        ApiResponse<Void> response = ApiResponse.error("Unsupported media type.", List.of(errorDetail), requestId);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    /**
     * Server fallback handler acting as a safety net for unexpected internal errors.
     *
     * @param ex any uncaught base exception
     * @return a {@link ResponseEntity} indicating internal server failure
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllUncaughtExceptions(Exception ex) {
        String requestId = generateRequestId();
        log.error("An unexpected internal error occurred [Request ID: {}]", requestId, ex);

        ApiResponse.ApiErrorDetail errorDetail = new ApiResponse.ApiErrorDetail(
                "INTERNAL_SERVER_ERROR",
                "server",
                "An unexpected condition occurred on the server. Please contact support."
        );

        ApiResponse<Void> response = ApiResponse.error("The request could not be processed due to an internal server error.", List.of(errorDetail), requestId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Dynamically generates a unique identifier for correlation and traceability purposes.
     *
     * @return a string representation of a randomized UUID
     */
    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }
}
