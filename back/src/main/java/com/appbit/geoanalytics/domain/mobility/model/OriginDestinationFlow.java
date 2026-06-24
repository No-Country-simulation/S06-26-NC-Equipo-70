package com.appbit.geoanalytics.domain.mobility.model;

import com.appbit.geoanalytics.domain.mobility.exception.MobilityDomainException;
import com.appbit.geoanalytics.domain.mobility.vo.FlowDistanceKm;
import com.appbit.geoanalytics.domain.mobility.vo.OriginDestinationFlowId;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.vo.GeoPoint;
import com.appbit.geoanalytics.domain.shared.vo.Period;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class OriginDestinationFlow {

    private final OriginDestinationFlowId id;
    private final DataSourceId sourceId;
    private final RegionId originRegionId;
    private final RegionId destinationRegionId;
    private final String originClusterName;
    private final String destinationClusterName;
    private final String originMunicipality;
    private final String destinationMunicipality;
    private final GeoPoint originPoint;
    private final GeoPoint destinationPoint;
    private final boolean sameCluster;
    private final long users;
    private final long trips;
    private final FlowDistanceKm averageDistanceKm;
    private final Period predominantPeriod;

    @Builder
    public OriginDestinationFlow(
            OriginDestinationFlowId id,
            DataSourceId sourceId,
            RegionId originRegionId,
            RegionId destinationRegionId,
            String originClusterName,
            String destinationClusterName,
            String originMunicipality,
            String destinationMunicipality,
            GeoPoint originPoint,
            GeoPoint destinationPoint,
            boolean sameCluster,
            long users,
            long trips,
            FlowDistanceKm averageDistanceKm,
            Period predominantPeriod
    ) {
        if (id == null) {
            throw new MobilityDomainException("Origin destination flow id cannot be null");
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

        if (originPoint == null) {
            throw new MobilityDomainException("Origin point cannot be null");
        }

        if (destinationPoint == null) {
            throw new MobilityDomainException("Destination point cannot be null");
        }

        if (averageDistanceKm == null) {
            throw new MobilityDomainException("Average distance km cannot be null");
        }

        if (predominantPeriod == null) {
            throw new MobilityDomainException("Predominant period cannot be null");
        }

        this.id = id;
        this.sourceId = sourceId;
        this.originRegionId = originRegionId;
        this.destinationRegionId = destinationRegionId;
        this.originClusterName = validateText(originClusterName, "Origin cluster name", 2, 40);
        this.destinationClusterName = validateText(destinationClusterName, "Destination cluster name", 2, 40);
        this.originMunicipality = validateText(originMunicipality, "Origin municipality", 2, 60);
        this.destinationMunicipality = validateText(destinationMunicipality, "Destination municipality", 2, 60);
        this.originPoint = originPoint;
        this.destinationPoint = destinationPoint;
        this.sameCluster = sameCluster;
        this.users = validateNonNegative(users, "Users");
        this.trips = validateNonNegative(trips, "Trips");
        this.averageDistanceKm = averageDistanceKm;
        this.predominantPeriod = predominantPeriod;
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

        OriginDestinationFlow that = (OriginDestinationFlow) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
