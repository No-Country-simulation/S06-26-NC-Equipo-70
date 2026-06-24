package com.appbit.geoanalytics.domain.concentration.vo;

import com.appbit.geoanalytics.domain.concentration.exception.ConcentrationDomainException;

import java.math.BigDecimal;

public record MetricRatio(BigDecimal value) {

    public MetricRatio {
        if (value == null) throw new ConcentrationDomainException("Metric ratio cannot be null");

        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(BigDecimal.ONE) > 0) {
            throw new ConcentrationDomainException("Metric ratio must be between 0 and 1");
        }

        if (value.scale() > 4) {
            throw new ConcentrationDomainException("Metric ratio scale cannot exceed 4 decimal places");
        }
    }
}
