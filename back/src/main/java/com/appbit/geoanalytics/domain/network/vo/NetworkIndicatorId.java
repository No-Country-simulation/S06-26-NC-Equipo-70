package com.appbit.geoanalytics.domain.network.vo;

import com.appbit.geoanalytics.domain.exception.IdentityRestrictionException;

import java.util.UUID;

public record NetworkIndicatorId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public NetworkIndicatorId {
        if (value == null) {
            throw new IdentityRestrictionException("NetworkIndicatorId cannot be null");
        }

        if (NIL_UUID.equals(value)) {
            throw new IdentityRestrictionException("NetworkIndicatorId cannot be nil UUID");
        }

        if (value.version() != 7) {
            throw new IdentityRestrictionException("NetworkIndicatorId must be UUIDv7. Received version: " + value.version());
        }

        if (value.variant() != 2) {
            throw new IdentityRestrictionException("NetworkIndicatorId must use RFC 4122/IETF variant. Received variant: " + value.variant());
        }
    }
}
