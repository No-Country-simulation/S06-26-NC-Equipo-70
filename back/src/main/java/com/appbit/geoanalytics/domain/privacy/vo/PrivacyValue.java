package com.appbit.geoanalytics.domain.privacy.vo;

import com.appbit.geoanalytics.domain.privacy.exception.PrivacyDomainException;

public record PrivacyValue(String value) {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 200;

    public PrivacyValue {
        if (value == null) {
            throw new PrivacyDomainException("Privacy value cannot be null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new PrivacyDomainException("Privacy value cannot be blank");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new PrivacyDomainException(
                    "Privacy value length must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters"
            );
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new PrivacyDomainException("Privacy value cannot contain control characters");
        }
    }
}
