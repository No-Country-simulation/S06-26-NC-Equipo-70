package com.appbit.geoanalytics.domain.privacy.vo;

import com.appbit.geoanalytics.domain.privacy.exception.PrivacyDomainException;

public record PrivacyParameter(String value) {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 80;

    public PrivacyParameter {
        if (value == null) {
            throw new PrivacyDomainException("Privacy parameter cannot be null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new PrivacyDomainException("Privacy parameter cannot be blank");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new PrivacyDomainException(
                    "Privacy parameter length must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters"
            );
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new PrivacyDomainException("Privacy parameter cannot contain control characters");
        }
    }
}
