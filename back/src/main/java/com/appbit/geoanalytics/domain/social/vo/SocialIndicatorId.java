package com.appbit.geoanalytics.domain.social.vo;

import com.appbit.geoanalytics.domain.social.exception.SocialDomainException;

import java.util.UUID;

public record SocialIndicatorId(UUID value) {
    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public SocialIndicatorId {
        if (value == null) throw new SocialDomainException("Social indicator id cannot be null");

        if (NIL_UUID.equals(value)) {
            throw new SocialDomainException("Social indicator id cannot be nil UUID");
        }

        if (value.version() != 7) {
            throw new SocialDomainException("Social indicator id must be UUIDv7");
        }

        if (value.variant() != 2) {
            throw new SocialDomainException("Social indicator id must be RFC 4122 compatible");
        }
    }
}
