package com.appbit.geoanalytics.infrastructure.identity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UuidV7GeneratorAdapterTest {

    private final UuidV7GeneratorAdapter generator = new UuidV7GeneratorAdapter();

    @Test
    void shouldGenerateRfc4122UuidV7() {
        UUID uuid = generator.generate();

        assertThat(uuid).isNotEqualTo(new UUID(0L, 0L));
        assertThat(uuid.version()).isEqualTo(7);
        assertThat(uuid.variant()).isEqualTo(2);
    }
}
