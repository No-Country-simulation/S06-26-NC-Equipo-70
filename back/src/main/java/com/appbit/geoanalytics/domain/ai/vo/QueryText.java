package com.appbit.geoanalytics.domain.ai.vo;

import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;

public record QueryText(String value) {

    public QueryText {
        if (value == null) throw new AiDomainException("Query text cannot be null");

        value = value.trim();

        if (value.isBlank()) throw new AiDomainException("Query text cannot be blank");

        if (value.length() < 3 || value.length() > 500) {
            throw new AiDomainException("Query text length must be between 3 and 500 characters");
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new AiDomainException("Query text cannot contain control characters");
        }
    }
}
