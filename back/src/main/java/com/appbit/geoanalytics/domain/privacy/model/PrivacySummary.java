package com.appbit.geoanalytics.domain.privacy.model;

import com.appbit.geoanalytics.domain.privacy.exception.PrivacyDomainException;
import com.appbit.geoanalytics.domain.privacy.vo.PrivacyParameter;
import com.appbit.geoanalytics.domain.privacy.vo.PrivacySummaryId;
import com.appbit.geoanalytics.domain.privacy.vo.PrivacyValue;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class PrivacySummary {

    private final PrivacySummaryId id;
    private final DataSourceId sourceId;
    private final PrivacyParameter parameter;
    private final PrivacyValue value;

    @Builder
    public PrivacySummary(
            PrivacySummaryId id,
            DataSourceId sourceId,
            PrivacyParameter parameter,
            PrivacyValue value
    ) {
        if (id == null) {
            throw new PrivacyDomainException("Privacy summary id cannot be null");
        }

        if (sourceId == null) {
            throw new PrivacyDomainException("Data source id cannot be null");
        }

        if (parameter == null) {
            throw new PrivacyDomainException("Privacy parameter cannot be null");
        }

        if (value == null) {
            throw new PrivacyDomainException("Privacy value cannot be null");
        }

        this.id = id;
        this.sourceId = sourceId;
        this.parameter = parameter;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrivacySummary that = (PrivacySummary) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
