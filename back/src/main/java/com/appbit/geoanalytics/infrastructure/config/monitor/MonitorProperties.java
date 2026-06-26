package com.appbit.geoanalytics.infrastructure.config.monitor;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "monitor")
public record MonitorProperties(@NotBlank String token) {}