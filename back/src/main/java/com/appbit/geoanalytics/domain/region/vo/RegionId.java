package com.appbit.geoanalytics.domain.region.vo;

import com.appbit.geoanalytics.domain.exception.IdentityRestrictionException;

import java.util.UUID;

public record RegionId(UUID value) {

    private static final UUID ZERO_UUID =
            new UUID(0L, 0L);

    public RegionId {
        if (value == null) throw new IdentityRestrictionException("RegionId cannot be null.");

        if (ZERO_UUID.equals(value)) throw new IdentityRestrictionException(
                "RegionId cannot be the zero UUID: 00000000-0000-0000-0000-000000000000.");

        if (value.version() != 7) throw new IdentityRestrictionException(
                "RegionId must be UUIDv7. Received value: version " + value.version() + ".");

        if (value.variant() != 2) throw new IdentityRestrictionException(
                "RegionId must use RFC 4122/IETF variant. Received value: variant " + value.variant() + ".");
    }
}