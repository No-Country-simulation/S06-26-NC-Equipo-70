package com.appbit.geoanalytics.infrastructure.adapter.in.web.monitor;

import com.appbit.geoanalytics.application.port.in.CheckDatabaseHealthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/uptime")
@RequiredArgsConstructor
public class UptimeMonitorController {

    private final CheckDatabaseHealthUseCase checkDatabaseHealthUseCase;

    @RequestMapping(
            value = "/db",
            method = {RequestMethod.GET, RequestMethod.HEAD}
    )
    public ResponseEntity<Void> checkDatabase() {
        boolean databaseAvailable = checkDatabaseHealthUseCase.isDatabaseAvailable();

        if (databaseAvailable) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
