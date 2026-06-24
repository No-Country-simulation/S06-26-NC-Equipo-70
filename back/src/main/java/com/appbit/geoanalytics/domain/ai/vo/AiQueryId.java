package com.appbit.geoanalytics.domain.ai.vo;

import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;

import java.util.UUID;

public record AiQueryId(UUID value) {
    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public AiQueryId {
        if (value == null) throw new AiDomainException("AI query id cannot be null");

        if (NIL_UUID.equals(value)) {
            throw new AiDomainException("AI query id cannot be nil UUID");
        }

        if (value.version() != 7) {
            throw new AiDomainException("AI query id must be UUIDv7");
        }

        if (value.variant() != 2) {
            throw new AiDomainException("AI query id must be RFC 4122 compatible");
        }
    }
}
