package com.appbit.geoanalytics.domain.mobility.vo;

import com.appbit.geoanalytics.domain.mobility.exception.MobilityDomainException;

import java.math.BigDecimal;

public record FlowDistanceKm(BigDecimal value) {

    private static final int MAX_SCALE = 3;

    public FlowDistanceKm {
        if (value == null) {
            throw new MobilityDomainException("Flow distance km cannot be null");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new MobilityDomainException("Flow distance km cannot be negative");
        }

        if (value.scale() > MAX_SCALE) {
            throw new MobilityDomainException("Flow distance km scale cannot exceed " + MAX_SCALE + " decimal places");
        }
    }
}
