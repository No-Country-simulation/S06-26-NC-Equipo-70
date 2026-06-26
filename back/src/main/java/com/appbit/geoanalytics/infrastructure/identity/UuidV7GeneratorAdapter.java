package com.appbit.geoanalytics.infrastructure.identity;

import com.appbit.geoanalytics.application.shared.port.out.IdGeneratorPort;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
public class UuidV7GeneratorAdapter implements IdGeneratorPort {

    private final UuidV7Generator uuidV7Generator = new UuidV7Generator(new SecureRandom());

    @Override
    public UUID generate() {
        return uuidV7Generator.generate();
    }
}

