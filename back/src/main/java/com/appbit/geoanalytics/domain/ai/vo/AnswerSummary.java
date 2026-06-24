package com.appbit.geoanalytics.domain.ai.vo;

import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;

public record AnswerSummary(String value) {

    public AnswerSummary {
        if (value == null) throw new AiDomainException("Answer summary cannot be null");

        value = value.trim();

        if (value.isBlank()) throw new AiDomainException("Answer summary cannot be blank");

        if (value.length() < 5 || value.length() > 1000) {
            throw new AiDomainException("Answer summary length must be between 5 and 1000 characters");
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new AiDomainException("Answer summary cannot contain control characters");
        }
    }
}
