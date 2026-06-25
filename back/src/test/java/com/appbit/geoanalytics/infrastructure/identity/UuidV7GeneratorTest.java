package com.appbit.geoanalytics.infrastructure.identity;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UuidV7GeneratorTest {

    @Test
    void shouldComposeUuidV7BitsFromTimestampAndRandomSections() {
        long timestamp = 0x0123_4567_89ABL;
        long randomA = 0x0BCDL;
        long randomB = 0x1234_5678_9ABCDEFL;
        UuidV7Generator generator = new UuidV7Generator(new FixedSecureRandom(randomA, randomB));

        UUID uuid = generator.generate(timestamp);

        long expectedMostSignificantBits = (timestamp << (UuidV7Generator.VERSION_BITS + UuidV7Generator.RANDOM_A_BITS))
                | UuidV7Generator.VERSION_VALUE
                | randomA;
        long expectedLeastSignificantBits = UuidV7Generator.VARIANT_VALUE | randomB;

        assertThat(uuid.getMostSignificantBits()).isEqualTo(expectedMostSignificantBits);
        assertThat(uuid.getLeastSignificantBits()).isEqualTo(expectedLeastSignificantBits);
        assertThat(uuid.version()).isEqualTo(UuidV7Generator.UUID_VERSION);
        assertThat(uuid.variant()).isEqualTo(2);
    }

    @Test
    void shouldMaskTimestampToFortyEightBits() {
        long timestampOverflow = (1L << UuidV7Generator.TIMESTAMP_BITS) | 0x1234L;
        UuidV7Generator generator = new UuidV7Generator(new FixedSecureRandom(0L, 0L));

        UUID uuid = generator.generate(timestampOverflow);

        long embeddedTimestamp = uuid.getMostSignificantBits() >>> (UuidV7Generator.VERSION_BITS + UuidV7Generator.RANDOM_A_BITS);
        assertThat(embeddedTimestamp).isEqualTo(0x1234L);
    }

    @Test
    void shouldRequestRandomValuesWithExpectedBounds() {
        BoundRecordingSecureRandom secureRandom = new BoundRecordingSecureRandom(1L, 2L);
        UuidV7Generator generator = new UuidV7Generator(secureRandom);

        generator.generate(0L);

        assertThat(secureRandom.bounds()).containsExactly(UuidV7Generator.RANDOM_A_BOUND, UuidV7Generator.RANDOM_B_BOUND);
    }

    @Test
    void shouldGenerateDifferentValues() {
        UuidV7Generator generator = new UuidV7Generator(new SecureRandom());
        Set<UUID> generated = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            generated.add(generator.generate());
        }

        assertThat(generated).hasSize(100);
    }

    @Test
    void shouldEmbedCurrentTimestampInMostSignificantBits() {
        UuidV7Generator generator = new UuidV7Generator(new SecureRandom());

        long beforeGeneration = System.currentTimeMillis();
        UUID uuid = generator.generate();
        long afterGeneration = System.currentTimeMillis();

        long embeddedTimestamp = uuid.getMostSignificantBits() >>> (UuidV7Generator.VERSION_BITS + UuidV7Generator.RANDOM_A_BITS);

        assertThat(embeddedTimestamp).isBetween(beforeGeneration, afterGeneration);
    }

    @Test
    void shouldRejectMissingEntropySource() {
        assertThatThrownBy(() -> new UuidV7Generator(null))
                .isInstanceOf(UuidV7GeneratorConfigurationException.class)
                .hasMessage("UUIDv7 generator requires a SecureRandom entropy source.");
    }

    private static class FixedSecureRandom extends SecureRandom {

        private final Queue<Long> values;

        FixedSecureRandom(long firstValue, long secondValue) {
            this.values = new ArrayDeque<>();
            this.values.add(firstValue);
            this.values.add(secondValue);
        }

        @Override
        public long nextLong(long bound) {
            long value = values.remove();
            assertThat(value).isBetween(0L, bound - 1);
            return value;
        }
    }

    private static final class BoundRecordingSecureRandom extends FixedSecureRandom {

        private final Queue<Long> bounds = new ArrayDeque<>();

        BoundRecordingSecureRandom(long firstValue, long secondValue) {
            super(firstValue, secondValue);
        }

        @Override
        public long nextLong(long bound) {
            bounds.add(bound);
            return super.nextLong(bound);
        }

        Queue<Long> bounds() {
            return bounds;
        }
    }
}
