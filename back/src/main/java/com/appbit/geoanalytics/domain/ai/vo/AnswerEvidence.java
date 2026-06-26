package com.appbit.geoanalytics.domain.ai.vo;

import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;

public record AnswerEvidence(String value) {

    public AnswerEvidence {
        if (value == null) throw new AiDomainException("Answer evidence cannot be null");

        value = value.trim();

        if (value.isBlank()) throw new AiDomainException("Answer evidence cannot be blank");

        if (value.length() < 5 || value.length() > 1000) {
            throw new AiDomainException("Answer evidence length must be between 5 and 1000 characters");
        }

        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new AiDomainException("Answer evidence cannot contain control characters");
        }
    }
}
