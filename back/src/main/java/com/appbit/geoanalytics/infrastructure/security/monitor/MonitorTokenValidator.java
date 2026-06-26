package com.appbit.geoanalytics.infrastructure.security.monitor;

import com.appbit.geoanalytics.infrastructure.config.monitor.MonitorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
@RequiredArgsConstructor
public class MonitorTokenValidator {

    private final MonitorProperties monitorProperties;

    public boolean isValid(String receivedToken) {
        if (receivedToken == null || receivedToken.isBlank()) {
            return false;
        }

        return MessageDigest.isEqual(
                receivedToken.getBytes(StandardCharsets.UTF_8),
                monitorProperties.token().getBytes(StandardCharsets.UTF_8)
        );
    }
}
