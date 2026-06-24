package com.appbit.geoanalytics.domain.mobility.vo;

import com.appbit.geoanalytics.domain.mobility.exception.MobilityDomainException;

import java.util.UUID;

public record OriginDestinationFlowId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public OriginDestinationFlowId {
        if (value == null) {
            throw new MobilityDomainException("Origin destination flow id cannot be null");
        }

        if (NIL_UUID.equals(value)) {
            throw new MobilityDomainException("Origin destination flow id cannot be nil UUID");
        }

        if (value.version() != 7) {
            throw new MobilityDomainException("Origin destination flow id must be UUIDv7");
        }

        if (value.variant() != 2) {
            throw new MobilityDomainException("Origin destination flow id must be RFC 4122 compatible");
        }
    }
}
