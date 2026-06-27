package com.appbit.geoanalytics.infrastructure.identity;

import java.security.SecureRandom;
import java.util.UUID;

final class UuidV7Generator {

    static final int TIMESTAMP_BITS = 48;
    static final int VERSION_BITS = 4;
    static final int RANDOM_A_BITS = 12;
    static final int RANDOM_B_BITS = 62;
    static final int UUID_VERSION = 7;
    static final int RFC_4122_VARIANT = 0b10;

    static final long TIMESTAMP_MASK = (1L << TIMESTAMP_BITS) - 1;
    static final long RANDOM_A_BOUND = 1L << RANDOM_A_BITS;
    static final long RANDOM_B_BOUND = 1L << RANDOM_B_BITS;
    static final long VERSION_VALUE = (long) UUID_VERSION << RANDOM_A_BITS;
    static final long VARIANT_VALUE = (long) RFC_4122_VARIANT << RANDOM_B_BITS;

    private final SecureRandom secureRandom;

    UuidV7Generator(SecureRandom secureRandom) {
        if (secureRandom == null) {
            throw new UuidV7GeneratorConfigurationException("UUIDv7 generator requires a SecureRandom entropy source.");
        }

        this.secureRandom = secureRandom;
    }

    UUID generate() {
        return generate(System.currentTimeMillis());
    }

    UUID generate(long unixTimestampMillis) {
        long timestamp = unixTimestampMillis & TIMESTAMP_MASK;
        long randomA = secureRandom.nextLong(RANDOM_A_BOUND);
        long randomB = secureRandom.nextLong(RANDOM_B_BOUND);

        long mostSignificantBits = (timestamp << (VERSION_BITS + RANDOM_A_BITS)) | VERSION_VALUE | randomA;
        long leastSignificantBits = VARIANT_VALUE | randomB;

        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}
