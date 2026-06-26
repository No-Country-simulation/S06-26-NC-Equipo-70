package com.appbit.geoanalytics.domain.shared.vo;

import com.appbit.geoanalytics.domain.shared.exception.SharedDomainException;

import java.math.BigDecimal;

public record IndicatorScore(BigDecimal value) {

    public IndicatorScore {
        if (value == null) throw new SharedDomainException("Indicator score cannot be null");

        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.ONE) > 0) {
            throw new SharedDomainException("Indicator score must be between 0 and 1");
        }

        if (value.scale() > 4) {
            throw new SharedDomainException("Indicator score scale cannot exceed 4 decimal places");
        }
    }
}
