package com.appbit.geoanalytics.domain.ingestion.enums;

public enum IngestionState {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    SKIPPED;

    public boolean isFinished() {
        return this == COMPLETED || this == FAILED || this == SKIPPED;
    }

    public boolean requiresErrorMessage() {
        return this == FAILED || this == SKIPPED;
    }
}