package com.appbit.geoanalytics.domain.mobility.model;

import com.appbit.geoanalytics.domain.mobility.exception.MobilityDomainException;
import com.appbit.geoanalytics.domain.mobility.vo.FlowDistanceKm;
import com.appbit.geoanalytics.domain.mobility.vo.TravelDistanceMetricId;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.vo.Period;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class TravelDistanceMetric {

    private final TravelDistanceMetricId id;
    private final DataSourceId sourceId;
    private final RegionId originRegionId;
    private final RegionId destinationRegionId;
    private final String originClusterName;
    private final String destinationClusterName;
    private final boolean sameCluster;
    private final long observations;
    private final FlowDistanceKm averageDistanceKm;
    private final FlowDistanceKm p25DistanceKm;
    private final FlowDistanceKm p75DistanceKm;
    private final Period predominantPeriod;

    @Builder
    public TravelDistanceMetric(
            TravelDistanceMetricId id,
            DataSourceId sourceId,
            RegionId originRegionId,
            RegionId destinationRegionId,
            String originClusterName,
            String destinationClusterName,
            boolean sameCluster,
            long observations,
            FlowDistanceKm averageDistanceKm,
            FlowDistanceKm p25DistanceKm,
            FlowDistanceKm p75DistanceKm,
            Period predominantPeriod
    ) {
        if (id == null) {
            throw new MobilityDomainException("Travel distance metric id cannot be null");
        }

        if (sourceId == null) {
            throw new MobilityDomainException("Data source id cannot be null");
        }

        if (originRegionId == null) {
            throw new MobilityDomainException("Origin region id cannot be null");
        }

        if (destinationRegionId == null) {
            throw new MobilityDomainException("Destination region id cannot be null");
        }

        if (averageDistanceKm == null) {
            throw new MobilityDomainException("Average distance km cannot be null");
        }

        if (p25DistanceKm == null) {
            throw new MobilityDomainException("P25 distance km cannot be null");
        }

        if (p75DistanceKm == null) {
            throw new MobilityDomainException("P75 distance km cannot be null");
        }

        if (predominantPeriod == null) {
            throw new MobilityDomainException("Predominant period cannot be null");
        }

        long normalizedObservations = validateNonNegative(observations, "Observations");

        validatePercentileConsistency(
                p25DistanceKm,
                averageDistanceKm,
                p75DistanceKm
        );

        this.id = id;
        this.sourceId = sourceId;
        this.originRegionId = originRegionId;
        this.destinationRegionId = destinationRegionId;
        this.originClusterName = validateText(originClusterName, "Origin cluster name", 2, 40);
        this.destinationClusterName = validateText(destinationClusterName, "Destination cluster name", 2, 40);
        this.sameCluster = sameCluster;
        this.observations = normalizedObservations;
        this.averageDistanceKm = averageDistanceKm;
        this.p25DistanceKm = p25DistanceKm;
        this.p75DistanceKm = p75DistanceKm;
        this.predominantPeriod = predominantPeriod;
    }

    private void validatePercentileConsistency(
            FlowDistanceKm p25DistanceKm,
            FlowDistanceKm averageDistanceKm,
            FlowDistanceKm p75DistanceKm
    ) {
        if (p25DistanceKm.value().compareTo(averageDistanceKm.value()) > 0) {
            throw new MobilityDomainException("P25 distance cannot be greater than average distance");
        }

        if (averageDistanceKm.value().compareTo(p75DistanceKm.value()) > 0) {
            throw new MobilityDomainException("Average distance cannot be greater than P75 distance");
        }
    }

    private long validateNonNegative(long value, String fieldName) {
        if (value < 0) {
            throw new MobilityDomainException(fieldName + " cannot be negative");
        }

        return value;
    }

    private String validateText(String value, String fieldName, int minLength, int maxLength) {
        if (value == null) {
            throw new MobilityDomainException(fieldName + " cannot be null");
        }

        String trimmed = value.trim();

        if (trimmed.isBlank()) {
            throw new MobilityDomainException(fieldName + " cannot be blank");
        }

        if (trimmed.length() < minLength || trimmed.length() > maxLength) {
            throw new MobilityDomainException(
                    fieldName + " length must be between " + minLength + " and " + maxLength + " characters"
            );
        }

        if (trimmed.chars().anyMatch(Character::isISOControl)) {
            throw new MobilityDomainException(fieldName + " cannot contain control characters");
        }

        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TravelDistanceMetric that = (TravelDistanceMetric) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
