package com.appbit.geoanalytics.domain.shared.vo;

import com.appbit.geoanalytics.domain.shared.enums.PeriodType;
import com.appbit.geoanalytics.domain.shared.exception.SharedDomainException;

public record Period(PeriodType value) {

    public Period {
        if (value == null) throw new SharedDomainException("Period cannot be null");
    }
}
