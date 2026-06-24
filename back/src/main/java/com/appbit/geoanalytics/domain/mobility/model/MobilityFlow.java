package com.appbit.geoanalytics.domain.mobility.model;

import com.appbit.geoanalytics.domain.mobility.exception.MobilityDomainException;
import com.appbit.geoanalytics.domain.mobility.vo.FlowDistanceKm;
import com.appbit.geoanalytics.domain.mobility.vo.FlowPercentage;
import com.appbit.geoanalytics.domain.mobility.vo.MobilityFlowId;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.vo.Ecgi;
import com.appbit.geoanalytics.domain.shared.vo.GeoPoint;
import com.appbit.geoanalytics.domain.shared.vo.Period;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class MobilityFlow {

    private final MobilityFlowId id;
    private final DataSourceId sourceId;
    private final RegionId originRegionId;
    private final RegionId destinationRegionId;
    private final Ecgi originEcgi;
    private final Ecgi destinationEcgi;
    private final GeoPoint originPoint;
    private final GeoPoint destinationPoint;
    private final String originClusterName;
    private final String destinationClusterName;
    private final String originMunicipality;
    private final String destinationMunicipality;
    private final long users;
    private final long transitions;
    private final FlowDistanceKm distanceKm;
    private final Period predominantPeriod;
    private final FlowPercentage originClusterPercentage;

    @Builder
    public MobilityFlow(
            MobilityFlowId id,
            DataSourceId sourceId,
            RegionId originRegionId,
            RegionId destinationRegionId,
            Ecgi originEcgi,
            Ecgi destinationEcgi,
            GeoPoint originPoint,
            GeoPoint destinationPoint,
            String originClusterName,
            String destinationClusterName,
            String originMunicipality,
            String destinationMunicipality,
            long users,
            long transitions,
            FlowDistanceKm distanceKm,
            Period predominantPeriod,
            FlowPercentage originClusterPercentage
    ) {
        if (id == null) {
            throw new MobilityDomainException("Mobility flow id cannot be null");
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

        if (originEcgi == null) {
            throw new MobilityDomainException("Origin ECGI cannot be null");
        }

        if (destinationEcgi == null) {
            throw new MobilityDomainException("Destination ECGI cannot be null");
        }

        if (originPoint == null) {
            throw new MobilityDomainException("Origin point cannot be null");
        }

        if (destinationPoint == null) {
            throw new MobilityDomainException("Destination point cannot be null");
        }

        if (distanceKm == null) {
            throw new MobilityDomainException("Distance km cannot be null");
        }

        if (predominantPeriod == null) {
            throw new MobilityDomainException("Predominant period cannot be null");
        }

        if (originClusterPercentage == null) {
            throw new MobilityDomainException("Origin cluster percentage cannot be null");
        }

        if (originEcgi.equals(destinationEcgi)) {
            throw new MobilityDomainException("Origin ECGI and destination ECGI cannot be the same");
        }

        long normalizedUsers = validateNonNegative(users, "Users");
        long normalizedTransitions = validateNonNegative(transitions, "Transitions");

        if (normalizedUsers > normalizedTransitions) {
            throw new MobilityDomainException("Users cannot be greater than transitions");
        }

        this.id = id;
        this.sourceId = sourceId;
        this.originRegionId = originRegionId;
        this.destinationRegionId = destinationRegionId;
        this.originEcgi = originEcgi;
        this.destinationEcgi = destinationEcgi;
        this.originPoint = originPoint;
        this.destinationPoint = destinationPoint;
        this.originClusterName = validateText(originClusterName, "Origin cluster name", 2, 40);
        this.destinationClusterName = validateText(destinationClusterName, "Destination cluster name", 2, 40);
        this.originMunicipality = validateText(originMunicipality, "Origin municipality", 2, 60);
        this.destinationMunicipality = validateText(destinationMunicipality, "Destination municipality", 2, 60);
        this.users = normalizedUsers;
        this.transitions = normalizedTransitions;
        this.distanceKm = distanceKm;
        this.predominantPeriod = predominantPeriod;
        this.originClusterPercentage = originClusterPercentage;
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

        MobilityFlow that = (MobilityFlow) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}