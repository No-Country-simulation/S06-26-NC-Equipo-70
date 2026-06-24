package com.appbit.geoanalytics.domain.mobility.model;

import com.appbit.geoanalytics.domain.mobility.exception.MobilityDomainException;
import com.appbit.geoanalytics.domain.mobility.vo.TravelDistanceMetricId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TravelDistanceMetricTest {

    @Test
    void shouldCreateValidMetricAndTrimTexts() {
        TravelDistanceMetric metric = new TravelDistanceMetric(
                travelDistanceMetricId(), dataSourceId(), regionId(), regionId(),
                "  Cluster Norte  ", "  Cluster Sur  ", false, 35L,
                flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period()
        );

        assertThat(metric.getOriginClusterName()).isEqualTo("Cluster Norte");
        assertThat(metric.getDestinationClusterName()).isEqualTo("Cluster Sur");
        assertThat(metric.getObservations()).isEqualTo(35L);
        assertThat(metric.isSameCluster()).isFalse();
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> metricWith(null, dataSourceId(), regionId(), regionId(), flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Travel distance metric id cannot be null");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), null, regionId(), regionId(), flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Data source id cannot be null");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), dataSourceId(), null, regionId(), flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Origin region id cannot be null");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), dataSourceId(), regionId(), null, flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Destination region id cannot be null");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), dataSourceId(), regionId(), regionId(), null, flowDistanceKm("8.000"), flowDistanceKm("18.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Average distance km cannot be null");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), dataSourceId(), regionId(), regionId(), flowDistanceKm("12.500"), null, flowDistanceKm("18.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("P25 distance km cannot be null");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), dataSourceId(), regionId(), regionId(), flowDistanceKm("12.500"), flowDistanceKm("8.000"), null, period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("P75 distance km cannot be null");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), dataSourceId(), regionId(), regionId(), flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), null))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Predominant period cannot be null");
    }

    @Test
    void shouldRejectInvalidMetricInvariants() {
        assertThatThrownBy(() -> metricWithObservations(-1L))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Observations cannot be negative");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), dataSourceId(), regionId(), regionId(), flowDistanceKm("12.500"), flowDistanceKm("13.000"), flowDistanceKm("18.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("P25 distance cannot be greater than average distance");
        assertThatThrownBy(() -> metricWith(travelDistanceMetricId(), dataSourceId(), regionId(), regionId(), flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("12.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Average distance cannot be greater than P75 distance");
    }

    @Test
    void shouldRejectInvalidTexts() {
        assertThatThrownBy(() -> metricWithTexts(null, "Cluster Sur")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name cannot be null");
        assertThatThrownBy(() -> metricWithTexts(" ", "Cluster Sur")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name cannot be blank");
        assertThatThrownBy(() -> metricWithTexts("A", "Cluster Sur")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> metricWithTexts(text(41), "Cluster Sur")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> metricWithTexts("Cluster\nNorte", "Cluster Sur")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name cannot contain control characters");
        assertThatThrownBy(() -> metricWithTexts("Cluster Norte", null)).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name cannot be null");
        assertThatThrownBy(() -> metricWithTexts("Cluster Norte", " ")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name cannot be blank");
        assertThatThrownBy(() -> metricWithTexts("Cluster Norte", "A")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> metricWithTexts("Cluster Norte", text(41))).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> metricWithTexts("Cluster Norte", "Cluster\nSur")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name cannot contain control characters");
    }

    @Test
    void shouldCompareMetricsByIdentity() {
        TravelDistanceMetricId sameId = travelDistanceMetricId();
        TravelDistanceMetric first = metricWith(sameId, dataSourceId(), regionId(), regionId(), flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period());
        TravelDistanceMetric second = metricWith(sameId, dataSourceId(), regionId(), regionId(), flowDistanceKm("13.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period());

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(travelDistanceMetric());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not a metric");
    }

    private TravelDistanceMetric metricWith(TravelDistanceMetricId id, com.appbit.geoanalytics.domain.source.vo.DataSourceId sourceId, com.appbit.geoanalytics.domain.region.vo.RegionId originRegionId, com.appbit.geoanalytics.domain.region.vo.RegionId destinationRegionId, com.appbit.geoanalytics.domain.mobility.vo.FlowDistanceKm averageDistanceKm, com.appbit.geoanalytics.domain.mobility.vo.FlowDistanceKm p25DistanceKm, com.appbit.geoanalytics.domain.mobility.vo.FlowDistanceKm p75DistanceKm, com.appbit.geoanalytics.domain.shared.vo.Period predominantPeriod) {
        return new TravelDistanceMetric(id, sourceId, originRegionId, destinationRegionId, "Cluster Norte", "Cluster Sur", false, 35L, averageDistanceKm, p25DistanceKm, p75DistanceKm, predominantPeriod);
    }

    private TravelDistanceMetric metricWithObservations(long observations) {
        return new TravelDistanceMetric(travelDistanceMetricId(), dataSourceId(), regionId(), regionId(), "Cluster Norte", "Cluster Sur", false, observations, flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period());
    }

    private TravelDistanceMetric metricWithTexts(String originClusterName, String destinationClusterName) {
        return new TravelDistanceMetric(travelDistanceMetricId(), dataSourceId(), regionId(), regionId(), originClusterName, destinationClusterName, false, 35L, flowDistanceKm("12.500"), flowDistanceKm("8.000"), flowDistanceKm("18.000"), period());
    }

    @Nested
    class TravelDistanceMetricIdTest {
        @Test
        void shouldAcceptUuidV7() {
            TravelDistanceMetricId id = new TravelDistanceMetricId(uuidV7());
            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new TravelDistanceMetricId(null)).isInstanceOf(MobilityDomainException.class).hasMessage("Travel distance metric id cannot be null");
            assertThatThrownBy(() -> new TravelDistanceMetricId(nilUuid())).isInstanceOf(MobilityDomainException.class).hasMessage("Travel distance metric id cannot be nil UUID");
            assertThatThrownBy(() -> new TravelDistanceMetricId(uuidV4())).isInstanceOf(MobilityDomainException.class).hasMessage("Travel distance metric id must be UUIDv7");
            assertThatThrownBy(() -> new TravelDistanceMetricId(nonRfc4122UuidV7())).isInstanceOf(MobilityDomainException.class).hasMessage("Travel distance metric id must be RFC 4122 compatible");
        }
    }
}
