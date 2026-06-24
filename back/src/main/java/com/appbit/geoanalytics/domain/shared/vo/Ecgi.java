package com.appbit.geoanalytics.domain.shared.vo;

import com.appbit.geoanalytics.domain.shared.exception.SharedDomainException;

public record Ecgi(String value) {

    private static final int MIN_LENGTH = 12;
    private static final int MAX_LENGTH = 16;

    public Ecgi {
        if (value == null) {
            throw new SharedDomainException("ECGI cannot be null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new SharedDomainException("ECGI cannot be blank");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new SharedDomainException(
                    "ECGI length must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters"
            );
        }

        if (!value.chars().allMatch(Character::isDigit)) {
            throw new SharedDomainException("ECGI must contain only digits");
        }

        if (value.chars().anyMatch(Character::isWhitespace)) {
            throw new SharedDomainException("ECGI cannot contain whitespace");
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new SharedDomainException("ECGI cannot contain control characters");
        }
    }
}