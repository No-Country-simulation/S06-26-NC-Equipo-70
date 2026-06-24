package com.appbit.geoanalytics.domain.network.vo;

import com.appbit.geoanalytics.domain.exception.IdentityRestrictionException;

import java.util.UUID;

public record AntennaId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public AntennaId {
        if (value == null) {
            throw new IdentityRestrictionException("AntennaId cannot be null");
        }

        if (NIL_UUID.equals(value)) {
            throw new IdentityRestrictionException("AntennaId cannot be nil UUID");
        }

        if (value.version() != 7) {
            throw new IdentityRestrictionException("AntennaId must be UUIDv7. Received version: " + value.version());
        }

        if (value.variant() != 2) {
            throw new IdentityRestrictionException("AntennaId must use RFC 4122/IETF variant. Received variant: " + value.variant());
        }
    }
}
