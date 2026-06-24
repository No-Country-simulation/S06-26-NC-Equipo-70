package com.appbit.geoanalytics.infrastructure.identity;

import com.appbit.geoanalytics.application.shared.port.out.IdGeneratorPort;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidV7GeneratorAdapter  implements IdGeneratorPort{

    @Override
    public UUID generate() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
