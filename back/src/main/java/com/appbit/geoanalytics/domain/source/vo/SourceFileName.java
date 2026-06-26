package com.appbit.geoanalytics.domain.source.vo;

import com.appbit.geoanalytics.domain.source.exception.SourceDomainException;

import java.util.Locale;

public record SourceFileName(String value) {

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 120;
    private static final String CSV_EXTENSION = ".csv";

    public SourceFileName {
        if (value == null) {
            throw new SourceDomainException("Source file name cannot be null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new SourceDomainException("Source file name cannot be blank");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new SourceDomainException(
                    "Source file name length must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters"
            );
        }

        if (value.contains("..")) {
            throw new SourceDomainException("Source file name cannot contain path traversal");
        }

        if (value.contains("/") || value.contains("\\")) {
            throw new SourceDomainException("Source file name cannot contain path separators");
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new SourceDomainException("Source file name cannot contain control characters");
        }

        if (!value.toLowerCase(Locale.ROOT).endsWith(CSV_EXTENSION)) {
            throw new SourceDomainException("Source file name must be a CSV file");
        }
    }
}
