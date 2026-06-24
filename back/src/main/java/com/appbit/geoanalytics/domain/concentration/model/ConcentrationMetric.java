package com.appbit.geoanalytics.domain.concentration.model;

import com.appbit.geoanalytics.domain.concentration.exception.ConcentrationDomainException;
import com.appbit.geoanalytics.domain.concentration.vo.ConcentrationMetricId;
import com.appbit.geoanalytics.domain.concentration.vo.MetricRatio;
import com.appbit.geoanalytics.domain.shared.vo.Period;
import com.appbit.geoanalytics.domain.shared.vo.Ecgi;
import com.appbit.geoanalytics.domain.shared.vo.GeoPoint;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public final class ConcentrationMetric {

    private final ConcentrationMetricId id;
    private final DataSourceId sourceId;
    private final RegionId regionId;
    private final Ecgi ecgi;
    private final String clusterName;
    private final String municipality;
    private final LocalDate dayDate;
    private final Period period;
    private final long activeUsers;
    private final long sessions;
    private final long downloadBytes;
    private final long uploadBytes;
    private final int averageSessionDurationSeconds;
    private final MetricRatio averageDropRate;
    private final MetricRatio averageCongestion;
    private final int totalCalls;
    private final int totalMessages;
    private final GeoPoint location;

    @Builder
    public ConcentrationMetric(
            ConcentrationMetricId id,
            DataSourceId sourceId,
            RegionId regionId,
            Ecgi ecgi,
            String clusterName,
            String municipality,
            LocalDate dayDate,
            Period period,
            long activeUsers,
            long sessions,
            long downloadBytes,
            long uploadBytes,
            int averageSessionDurationSeconds,
            MetricRatio averageDropRate,
            MetricRatio averageCongestion,
            int totalCalls,
            int totalMessages,
            GeoPoint location
    ) {
        if (id == null) {
            throw new ConcentrationDomainException("Concentration metric id cannot be null");
        }

        if (sourceId == null) {
            throw new ConcentrationDomainException("Data source id cannot be null");
        }

        if (regionId == null) {
            throw new ConcentrationDomainException("Region id cannot be null");
        }

        if (ecgi == null) {
            throw new ConcentrationDomainException("ECGI cannot be null");
        }

        if (dayDate == null) {
            throw new ConcentrationDomainException("Day date cannot be null");
        }

        if (period == null) {
            throw new ConcentrationDomainException("Period cannot be null");
        }

        if (averageDropRate == null) {
            throw new ConcentrationDomainException("Average drop rate cannot be null");
        }

        if (averageCongestion == null) {
            throw new ConcentrationDomainException("Average congestion cannot be null");
        }

        if (location == null) {
            throw new ConcentrationDomainException("Location cannot be null");
        }

        this.id = id;
        this.sourceId = sourceId;
        this.regionId = regionId;
        this.ecgi = ecgi;
        this.clusterName = validateText(clusterName, "Cluster name", 2, 40);
        this.municipality = validateText(municipality, "Municipality", 2, 60);
        this.dayDate = dayDate;
        this.period = period;
        this.activeUsers = validateNonNegative(activeUsers, "Active users");
        this.sessions = validateNonNegative(sessions, "Sessions");
        this.downloadBytes = validateNonNegative(downloadBytes, "Download bytes");
        this.uploadBytes = validateNonNegative(uploadBytes, "Upload bytes");
        this.averageSessionDurationSeconds = validateNonNegative(averageSessionDurationSeconds, "Average session duration seconds");
        this.averageDropRate = averageDropRate;
        this.averageCongestion = averageCongestion;
        this.totalCalls = validateNonNegative(totalCalls, "Total calls");
        this.totalMessages = validateNonNegative(totalMessages, "Total messages");
        this.location = location;
    }

    private String validateText(String value, String fieldName, int minLength, int maxLength) {
        if (value == null) {
            throw new ConcentrationDomainException(fieldName + " cannot be null");
        }

        String trimmed = value.trim();

        if (trimmed.isBlank()) {
            throw new ConcentrationDomainException(fieldName + " cannot be blank");
        }

        if (trimmed.length() < minLength || trimmed.length() > maxLength) {
            throw new ConcentrationDomainException(
                    fieldName + " length must be between " + minLength + " and " + maxLength + " characters"
            );
        }

        if (trimmed.chars().anyMatch(Character::isISOControl)) {
            throw new ConcentrationDomainException(fieldName + " cannot contain control characters");
        }

        return trimmed;
    }

    private long validateNonNegative(long value, String fieldName) {
        if (value < 0) {
            throw new ConcentrationDomainException(fieldName + " cannot be negative");
        }

        return value;
    }

    private int validateNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new ConcentrationDomainException(fieldName + " cannot be negative");
        }

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConcentrationMetric that = (ConcentrationMetric) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
