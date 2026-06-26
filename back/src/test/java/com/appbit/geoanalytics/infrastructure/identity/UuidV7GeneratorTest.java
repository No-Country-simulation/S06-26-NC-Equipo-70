package com.appbit.geoanalytics.infrastructure.identity;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
    void shouldGenerateUniqueRfc4122UuidV7ValuesConcurrently() throws Exception {
        int threadCount = 16;
        int idsPerThread = 1_000;
        int expectedTotal = threadCount * idsPerThread;
        UuidV7Generator generator = new UuidV7Generator(new SecureRandom());
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);

        try {
            List<Future<List<UUID>>> futures = new ArrayList<>(threadCount);

            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(generateUuidsWhenStarted(generator, ready, start, idsPerThread)));
            }

            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            List<UUID> generated = new ArrayList<>(expectedTotal);
            for (Future<List<UUID>> future : futures) {
                generated.addAll(future.get());
            }

            assertThat(generated).hasSize(expectedTotal);
            assertThat(new HashSet<>(generated)).hasSize(expectedTotal);
            assertThat(generated)
                    .allSatisfy(uuid -> {
                        assertThat(uuid).isNotEqualTo(new UUID(0L, 0L));
                        assertThat(uuid.version()).isEqualTo(UuidV7Generator.UUID_VERSION);
                        assertThat(uuid.variant()).isEqualTo(2);
                    });
        } finally {
            executor.shutdownNow();
        }
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

    private static Callable<List<UUID>> generateUuidsWhenStarted(
            UuidV7Generator generator, CountDownLatch ready, CountDownLatch start, int idsPerThread) {
        return () -> {
            ready.countDown();
            assertThat(start.await(5, TimeUnit.SECONDS)).isTrue();

            List<UUID> generated = new ArrayList<>(idsPerThread);
            for (int i = 0; i < idsPerThread; i++) {
                generated.add(generator.generate());
            }

            return generated;
        };
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
