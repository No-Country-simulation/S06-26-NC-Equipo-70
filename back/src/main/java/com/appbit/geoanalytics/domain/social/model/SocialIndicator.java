package com.appbit.geoanalytics.domain.social.model;

import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.enums.ConfidenceLevel;
import com.appbit.geoanalytics.domain.shared.enums.GapLevel;
import com.appbit.geoanalytics.domain.shared.enums.IndicatorUnit;
import com.appbit.geoanalytics.domain.shared.vo.IndicatorScore;
import com.appbit.geoanalytics.domain.social.enums.SocialIndicatorType;
import com.appbit.geoanalytics.domain.social.exception.SocialDomainException;
import com.appbit.geoanalytics.domain.social.vo.SocialIndicatorId;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class SocialIndicator {

    private final SocialIndicatorId id;
    private final RegionId regionId;
    private final DataSourceId sourceId;
    private final SocialIndicatorType indicatorType;
    private final IndicatorScore score;
    private final IndicatorUnit unit;
    private final GapLevel gapLevel;
    private final ConfidenceLevel confidenceLevel;
    private final String description;

    @Builder
    public SocialIndicator(
            SocialIndicatorId id,
            RegionId regionId,
            DataSourceId sourceId,
            SocialIndicatorType indicatorType,
            IndicatorScore score,
            IndicatorUnit unit,
            GapLevel gapLevel,
            ConfidenceLevel confidenceLevel,
            String description
    ) {
        if (id == null) {
            throw new SocialDomainException("Social indicator id cannot be null");
        }

        if (regionId == null) {
            throw new SocialDomainException("Region id cannot be null");
        }

        if (sourceId == null) {
            throw new SocialDomainException("Data source id cannot be null");
        }

        if (indicatorType == null) {
            throw new SocialDomainException("Social indicator type cannot be null");
        }

        if (score == null) {
            throw new SocialDomainException("Indicator score cannot be null");
        }

        if (unit == null) {
            throw new SocialDomainException("Indicator unit cannot be null");
        }

        if (gapLevel == null) {
            throw new SocialDomainException("Gap level cannot be null");
        }

        if (confidenceLevel == null) {
            throw new SocialDomainException("Confidence level cannot be null");
        }

        this.id = id;
        this.regionId = regionId;
        this.sourceId = sourceId;
        this.indicatorType = indicatorType;
        this.score = score;
        this.unit = unit;
        this.gapLevel = gapLevel;
        this.confidenceLevel = confidenceLevel;
        this.description = validateDescription(description);
    }

    private String validateDescription(String value) {
        if (value == null) {
            throw new SocialDomainException("Description cannot be null");
        }

        String trimmed = value.trim();

        if (trimmed.isBlank()) {
            throw new SocialDomainException("Description cannot be blank");
        }

        if (trimmed.length() < 5 || trimmed.length() > 500) {
            throw new SocialDomainException("Description length must be between 5 and 500 characters");
        }

        if (trimmed.chars().anyMatch(Character::isISOControl)) {
            throw new SocialDomainException("Description cannot contain control characters");
        }

        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialIndicator that = (SocialIndicator) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
