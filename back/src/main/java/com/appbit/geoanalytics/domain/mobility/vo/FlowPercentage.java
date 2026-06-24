package com.appbit.geoanalytics.domain.mobility.vo;

import com.appbit.geoanalytics.domain.mobility.exception.MobilityDomainException;

import java.math.BigDecimal;

public record FlowPercentage(BigDecimal value) {

    private static final int MAX_SCALE = 3;
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public FlowPercentage {
        if (value == null) {
            throw new MobilityDomainException("Flow percentage cannot be null");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(ONE_HUNDRED) > 0) {
            throw new MobilityDomainException("Flow percentage must be between 0 and 100");
        }

        if (value.scale() > MAX_SCALE) {
            throw new MobilityDomainException("Flow percentage scale cannot exceed " + MAX_SCALE + " decimal places");
        }
    }
}
