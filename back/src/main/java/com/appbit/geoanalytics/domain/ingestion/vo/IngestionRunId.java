package com.appbit.geoanalytics.domain.ingestion.vo;

import com.appbit.geoanalytics.domain.ingestion.exception.IngestionDomainException;

import java.util.UUID;

public record IngestionRunId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public IngestionRunId {
        if (value == null) {
            throw new IngestionDomainException("Ingestion run id cannot be null");
        }

        if (NIL_UUID.equals(value)) {
            throw new IngestionDomainException("Ingestion run id cannot be nil UUID");
        }

        if (value.version() != 7) {
            throw new IngestionDomainException("Ingestion run id must be UUIDv7");
        }

        if (value.variant() != 2) {
            throw new IngestionDomainException("Ingestion run id must be RFC 4122 compatible");
        }
    }
}
