package com.appbit.geoanalytics.infrastructure.adapter.in.rest.diagnostic;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/v1/diagnostics/exceptions")
public class ExceptionDiagnosticController {

    @GetMapping("/missing-query-param")
    public String missingQueryParam(@RequestParam String required) {
        return required;
    }

    @GetMapping("/type-mismatch")
    public Integer typeMismatch(@RequestParam Integer value) {
        return value;
    }

    @GetMapping("/constraint-violation")
    public Integer constraintViolation(@RequestParam @Min(1) Integer value) {
        return value;
    }

    @PostMapping(
            value = "/validation",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DiagnosticResponse validation(@Valid @RequestBody DiagnosticRequest request) {
        return new DiagnosticResponse(request.name(), request.quantity());
    }

    @PostMapping(
            value = "/post-only",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DiagnosticResponse postOnly(@RequestBody DiagnosticRequest request) {
        return new DiagnosticResponse(request.name(), request.quantity());
    }

    @GetMapping("/unexpected")
    public void unexpected() {
        throw new IllegalStateException("Forced unexpected exception for diagnostic testing.");
    }

    public record DiagnosticRequest(
            @NotBlank(message = "name must not be blank")
            String name,

            @Min(value = 1, message = "quantity must be greater than or equal to 1")
            Integer quantity
    ) {
    }

    public record DiagnosticResponse(
            String name,
            Integer quantity
    ) {
    }
}
