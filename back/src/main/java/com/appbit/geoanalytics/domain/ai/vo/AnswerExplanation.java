package com.appbit.geoanalytics.domain.ai.vo;

import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;

public record AnswerExplanation(String value) {

    public AnswerExplanation {
        if (value == null) throw new AiDomainException("Answer explanation cannot be null");

        value = value.trim();

        if (value.isBlank()) throw new AiDomainException("Answer explanation cannot be blank");

        if (value.length() < 10 || value.length() > 4000) {
            throw new AiDomainException("Answer explanation length must be between 10 and 4000 characters");
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new AiDomainException("Answer explanation cannot contain control characters");
        }
    }
}
