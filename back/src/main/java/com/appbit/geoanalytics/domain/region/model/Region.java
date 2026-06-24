package com.appbit.geoanalytics.domain.region.model;

import com.appbit.geoanalytics.domain.region.exception.RegionDomainException;
import com.appbit.geoanalytics.domain.shared.vo.GeoPoint;
import com.appbit.geoanalytics.domain.region.vo.RegionCode;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class Region {
    private final RegionId id;
    private final RegionCode code;
    private final String clusterName;
    private final String name;
    private final String municipality;
    private final GeoPoint center;

    @Builder
    public Region(
            RegionId id,
            RegionCode code,
            String clusterName,
            String name,
            String municipality,
            GeoPoint center
    ) {
        if (id == null) throw new RegionDomainException("Region id cannot be null");
        if (code == null) throw new RegionDomainException("Region code cannot be null");
        if (center == null) throw new RegionDomainException("Region coordinates cannot be null");
        this.id = id;
        this.code = code;
        this.name = validateName(name);
        this.municipality = validateMunicipality(municipality);
        this.clusterName = validateClusterName(clusterName);
        this.center = center;
    }

    private String validateName(String name) {
        if (name == null) throw new RegionDomainException("Region name cannot be null");
        String trimmed = name.trim();
        if (trimmed.isBlank()) throw new RegionDomainException("Region name cannot be blank");
        if (trimmed.length() < 2 || trimmed.length() > 80)
            throw new RegionDomainException("Region name length must be between 2 and 80 characters");
        return trimmed;
    }

    private String validateMunicipality(String municipality) {
        if (municipality == null) throw new RegionDomainException("Municipality cannot be null");
        String trimmed = municipality.trim();
        if (trimmed.isBlank()) throw new RegionDomainException("Municipality cannot be blank");
        if (trimmed.length() < 2 || trimmed.length() > 60)
            throw new RegionDomainException("Municipality length must be between 2 and 60 characters");
        return trimmed;
    }

    private String validateClusterName(String clusterName) {
        if (clusterName == null) throw new RegionDomainException("ClusterName cannot be null");
        String trimmed = clusterName.trim();
        if (trimmed.isBlank()) throw new RegionDomainException("ClusterName cannot be blank");
        if (trimmed.length() < 2 || trimmed.length() > 40)
            throw new RegionDomainException("ClusterName length must be between 2 and 40 characters");
        return trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return id.equals(region.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
