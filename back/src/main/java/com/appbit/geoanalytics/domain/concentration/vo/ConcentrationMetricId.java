package com.appbit.geoanalytics.domain.concentration.vo;

import com.appbit.geoanalytics.domain.concentration.exception.ConcentrationDomainException;

import java.util.UUID;

public record ConcentrationMetricId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public ConcentrationMetricId {
        if (value == null) {
            throw new ConcentrationDomainException("Concentration metric id cannot be null");
        }

        if (NIL_UUID.equals(value)) {
            throw new ConcentrationDomainException("Concentration metric id cannot be nil UUID");
        }

        if (value.version() != 7) {
            throw new ConcentrationDomainException("Concentration metric id must be UUIDv7");
        }

        if (value.variant() != 2) {
            throw new ConcentrationDomainException("Concentration metric id must be RFC 4122 compatible");
        }
    }
}
