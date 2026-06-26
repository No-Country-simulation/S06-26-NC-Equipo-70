package com.appbit.geoanalytics.domain.network.model;

import com.appbit.geoanalytics.domain.network.exception.NetworkDomainException;
import com.appbit.geoanalytics.domain.network.vo.AntennaId;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.vo.Ecgi;
import com.appbit.geoanalytics.domain.shared.vo.GeoPoint;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class Antenna {

    private final AntennaId id;
    private final Ecgi ecgi;
    private final RegionId regionId;
    private final String clusterName;
    private final String municipality;
    private final GeoPoint location;
    private final DataSourceId sourceId;

    @Builder
    public Antenna(
            AntennaId id,
            Ecgi ecgi,
            RegionId regionId,
            String clusterName,
            String municipality,
            GeoPoint location,
            DataSourceId sourceId
    ) {
        if (id == null) {
            throw new NetworkDomainException("Antenna id cannot be null");
        }

        if (ecgi == null) {
            throw new NetworkDomainException("ECGI cannot be null");
        }

        if (regionId == null) {
            throw new NetworkDomainException("Region id cannot be null");
        }

        if (location == null) {
            throw new NetworkDomainException("Antenna location cannot be null");
        }

        if (sourceId == null) {
            throw new NetworkDomainException("Data source id cannot be null");
        }

        this.id = id;
        this.ecgi = ecgi;
        this.regionId = regionId;
        this.clusterName = validateText(clusterName, "Cluster name", 2, 40);
        this.municipality = validateText(municipality, "Municipality", 2, 60);
        this.location = location;
        this.sourceId = sourceId;
    }

    private String validateText(String value, String fieldName, int minLength, int maxLength) {
        if (value == null) {
            throw new NetworkDomainException(fieldName + " cannot be null");
        }

        String trimmed = value.trim();

        if (trimmed.isBlank()) {
            throw new NetworkDomainException(fieldName + " cannot be blank");
        }

        if (trimmed.length() < minLength || trimmed.length() > maxLength) {
            throw new NetworkDomainException(
                    fieldName + " length must be between " + minLength + " and " + maxLength + " characters"
            );
        }

        if (trimmed.chars().anyMatch(Character::isISOControl)) {
            throw new NetworkDomainException(fieldName + " cannot contain control characters");
        }

        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Antenna antenna = (Antenna) o;

        return id.equals(antenna.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
