package com.appbit.geoanalytics.application.usecase;

import com.appbit.geoanalytics.application.port.in.CheckDatabaseHealthUseCase;
import com.appbit.geoanalytics.application.port.out.DatabaseHealthPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckDatabaseHealthService implements CheckDatabaseHealthUseCase {
    private final DatabaseHealthPort databaseHealthPort;

    @Override
    public boolean isDatabaseAvailable() {
        return databaseHealthPort.isDatabaseAvailable();
    }
}
