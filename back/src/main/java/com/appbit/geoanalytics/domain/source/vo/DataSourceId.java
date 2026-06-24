package com.appbit.geoanalytics.domain.source.vo;

import com.appbit.geoanalytics.domain.exception.IdentityRestrictionException;

import java.util.UUID;

public record DataSourceId(UUID value) {

    private static final UUID ZERO_UUID =
            new UUID(0L, 0L);

    public DataSourceId {
        if (value == null) throw new IdentityRestrictionException("DataSourceId cannot be null.");

        if (ZERO_UUID.equals(value)) throw new IdentityRestrictionException(
                "DataSourceId cannot be the zero UUID: 00000000-0000-0000-0000-000000000000.");

        if (value.version() != 7) throw new IdentityRestrictionException(
                "DataSourceId must be UUIDv7. Received value: version " + value.version() + ".");

        if (value.variant() != 2) throw new IdentityRestrictionException(
                "DataSourceId must use RFC 4122/IETF variant. Received value: variant " + value.variant() + ".");
    }
}
