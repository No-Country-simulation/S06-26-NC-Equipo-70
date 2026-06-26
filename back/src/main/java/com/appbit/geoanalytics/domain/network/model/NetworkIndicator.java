package com.appbit.geoanalytics.domain.network.model;

import com.appbit.geoanalytics.domain.network.enums.NetworkIndicatorType;
import com.appbit.geoanalytics.domain.network.exception.NetworkDomainException;
import com.appbit.geoanalytics.domain.network.vo.NetworkIndicatorId;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.enums.ConfidenceLevel;
import com.appbit.geoanalytics.domain.shared.enums.GapLevel;
import com.appbit.geoanalytics.domain.shared.enums.IndicatorUnit;
import com.appbit.geoanalytics.domain.shared.vo.IndicatorScore;
import com.appbit.geoanalytics.domain.shared.vo.Period;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class NetworkIndicator {

    private final NetworkIndicatorId id;
    private final RegionId regionId;
    private final DataSourceId sourceId;
    private final NetworkIndicatorType indicatorType;
    private final IndicatorScore score;
    private final IndicatorUnit unit;
    private final GapLevel gapLevel;
    private final ConfidenceLevel confidenceLevel;
    private final Period period;
    private final String description;

    @Builder
    public NetworkIndicator(
            NetworkIndicatorId id,
            RegionId regionId,
            DataSourceId sourceId,
            NetworkIndicatorType indicatorType,
            IndicatorScore score,
            IndicatorUnit unit,
            GapLevel gapLevel,
            ConfidenceLevel confidenceLevel,
            Period period,
            String description
    ) {
        if (id == null) {
            throw new NetworkDomainException("Network indicator id cannot be null");
        }

        if (regionId == null) {
            throw new NetworkDomainException("Region id cannot be null");
        }

        if (sourceId == null) {
            throw new NetworkDomainException("Data source id cannot be null");
        }

        if (indicatorType == null) {
            throw new NetworkDomainException("Network indicator type cannot be null");
        }

        if (score == null) {
            throw new NetworkDomainException("Indicator score cannot be null");
        }

        if (unit == null) {
            throw new NetworkDomainException("Indicator unit cannot be null");
        }

        if (gapLevel == null) {
            throw new NetworkDomainException("Gap level cannot be null");
        }

        if (confidenceLevel == null) {
            throw new NetworkDomainException("Confidence level cannot be null");
        }

        if (period == null) {
            throw new NetworkDomainException("Period cannot be null");
        }

        this.id = id;
        this.regionId = regionId;
        this.sourceId = sourceId;
        this.indicatorType = indicatorType;
        this.score = score;
        this.unit = unit;
        this.gapLevel = gapLevel;
        this.confidenceLevel = confidenceLevel;
        this.period = period;
        this.description = validateDescription(description);
    }

    private String validateDescription(String value) {
        if (value == null) {
            throw new NetworkDomainException("Description cannot be null");
        }

        String trimmed = value.trim();

        if (trimmed.isBlank()) {
            throw new NetworkDomainException("Description cannot be blank");
        }

        if (trimmed.length() < 5 || trimmed.length() > 500) {
            throw new NetworkDomainException("Description length must be between 5 and 500 characters");
        }

        if (trimmed.chars().anyMatch(Character::isISOControl)) {
            throw new NetworkDomainException("Description cannot contain control characters");
        }

        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkIndicator that = (NetworkIndicator) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}