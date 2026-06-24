package com.appbit.geoanalytics.domain.privacy.vo;

import com.appbit.geoanalytics.domain.privacy.exception.PrivacyDomainException;

import java.util.UUID;

public record PrivacySummaryId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public PrivacySummaryId {
        if (value == null) {
            throw new PrivacyDomainException("Privacy summary id cannot be null");
        }

        if (NIL_UUID.equals(value)) {
            throw new PrivacyDomainException("Privacy summary id cannot be nil UUID");
        }

        if (value.version() != 7) {
            throw new PrivacyDomainException("Privacy summary id must be UUIDv7");
        }

        if (value.variant() != 2) {
            throw new PrivacyDomainException("Privacy summary id must be RFC 4122 compatible");
        }
    }
}
