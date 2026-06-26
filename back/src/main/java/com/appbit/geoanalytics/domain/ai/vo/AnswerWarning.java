package com.appbit.geoanalytics.domain.ai.vo;

import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;

public record AnswerWarning(String value) {

    public AnswerWarning {
        if (value == null) throw new AiDomainException("Answer warning cannot be null");

        value = value.trim();

        if (value.isBlank()) throw new AiDomainException("Answer warning cannot be blank");

        if (value.length() < 5 || value.length() > 500) {
            throw new AiDomainException("Answer warning length must be between 5 and 500 characters");
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new AiDomainException("Answer warning cannot contain control characters");
        }
    }
}
