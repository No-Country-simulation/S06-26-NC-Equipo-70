package com.appbit.geoanalytics.domain.mobility.model;

import com.appbit.geoanalytics.domain.mobility.exception.MobilityDomainException;
import com.appbit.geoanalytics.domain.mobility.vo.FlowDistanceKm;
import com.appbit.geoanalytics.domain.mobility.vo.OriginDestinationFlowId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OriginDestinationFlowTest {

    @Test
    void shouldCreateValidFlowAndTrimTexts() {
        OriginDestinationFlow flow = new OriginDestinationFlow(
                originDestinationFlowId(), dataSourceId(), regionId(), regionId(),
                "  Cluster Norte  ", "  Cluster Sur  ", "  Cordoba  ", "  Villa Maria  ",
                geoPoint(), destinationGeoPoint(), false, 20L, 35L, flowDistanceKm("12.500"), period()
        );

        assertThat(flow.getOriginClusterName()).isEqualTo("Cluster Norte");
        assertThat(flow.getDestinationClusterName()).isEqualTo("Cluster Sur");
        assertThat(flow.getOriginMunicipality()).isEqualTo("Cordoba");
        assertThat(flow.getDestinationMunicipality()).isEqualTo("Villa Maria");
        assertThat(flow.isSameCluster()).isFalse();
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> flowWith(null, dataSourceId(), regionId(), regionId(), geoPoint(), destinationGeoPoint(), flowDistanceKm("1.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Origin destination flow id cannot be null");
        assertThatThrownBy(() -> flowWith(originDestinationFlowId(), null, regionId(), regionId(), geoPoint(), destinationGeoPoint(), flowDistanceKm("1.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Data source id cannot be null");
        assertThatThrownBy(() -> flowWith(originDestinationFlowId(), dataSourceId(), null, regionId(), geoPoint(), destinationGeoPoint(), flowDistanceKm("1.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Origin region id cannot be null");
        assertThatThrownBy(() -> flowWith(originDestinationFlowId(), dataSourceId(), regionId(), null, geoPoint(), destinationGeoPoint(), flowDistanceKm("1.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Destination region id cannot be null");
        assertThatThrownBy(() -> flowWith(originDestinationFlowId(), dataSourceId(), regionId(), regionId(), null, destinationGeoPoint(), flowDistanceKm("1.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Origin point cannot be null");
        assertThatThrownBy(() -> flowWith(originDestinationFlowId(), dataSourceId(), regionId(), regionId(), geoPoint(), null, flowDistanceKm("1.000"), period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Destination point cannot be null");
        assertThatThrownBy(() -> flowWith(originDestinationFlowId(), dataSourceId(), regionId(), regionId(), geoPoint(), destinationGeoPoint(), null, period()))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Average distance km cannot be null");
        assertThatThrownBy(() -> flowWith(originDestinationFlowId(), dataSourceId(), regionId(), regionId(), geoPoint(), destinationGeoPoint(), flowDistanceKm("1.000"), null))
                .isInstanceOf(MobilityDomainException.class).hasMessage("Predominant period cannot be null");
    }

    @Test
    void shouldRejectNegativeCounters() {
        assertThatThrownBy(() -> flowWithCounters(-1L, 35L)).isInstanceOf(MobilityDomainException.class).hasMessage("Users cannot be negative");
        assertThatThrownBy(() -> flowWithCounters(20L, -1L)).isInstanceOf(MobilityDomainException.class).hasMessage("Trips cannot be negative");
    }

    @Test
    void shouldRejectInvalidTexts() {
        assertThatThrownBy(() -> flowWithTexts(null, "Cluster Sur", "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name cannot be null");
        assertThatThrownBy(() -> flowWithTexts(" ", "Cluster Sur", "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name cannot be blank");
        assertThatThrownBy(() -> flowWithTexts("A", "Cluster Sur", "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> flowWithTexts(text(41), "Cluster Sur", "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster\nNorte", "Cluster Sur", "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin cluster name cannot contain control characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", null, "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name cannot be null");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", " ", "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name cannot be blank");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "A", "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", text(41), "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster\nSur", "Cordoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination cluster name cannot contain control characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", null, "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin municipality cannot be null");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", " ", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin municipality cannot be blank");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", "A", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", text(61), "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", "Cor\ndoba", "Villa Maria")).isInstanceOf(MobilityDomainException.class).hasMessage("Origin municipality cannot contain control characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", "Cordoba", null)).isInstanceOf(MobilityDomainException.class).hasMessage("Destination municipality cannot be null");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", "Cordoba", " ")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination municipality cannot be blank");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", "Cordoba", "A")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", "Cordoba", text(61))).isInstanceOf(MobilityDomainException.class).hasMessage("Destination municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> flowWithTexts("Cluster Norte", "Cluster Sur", "Cordoba", "Villa\nMaria")).isInstanceOf(MobilityDomainException.class).hasMessage("Destination municipality cannot contain control characters");
    }

    @Test
    void shouldCompareFlowsByIdentity() {
        OriginDestinationFlowId sameId = originDestinationFlowId();
        OriginDestinationFlow first = flowWith(sameId, dataSourceId(), regionId(), regionId(), geoPoint(), destinationGeoPoint(), flowDistanceKm("1.000"), period());
        OriginDestinationFlow second = flowWith(sameId, dataSourceId(), regionId(), regionId(), geoPoint(), destinationGeoPoint(), flowDistanceKm("2.000"), period());

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(originDestinationFlow());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not a flow");
    }

    private OriginDestinationFlow flowWith(OriginDestinationFlowId id, com.appbit.geoanalytics.domain.source.vo.DataSourceId sourceId, com.appbit.geoanalytics.domain.region.vo.RegionId originRegionId, com.appbit.geoanalytics.domain.region.vo.RegionId destinationRegionId, com.appbit.geoanalytics.domain.shared.vo.GeoPoint originPoint, com.appbit.geoanalytics.domain.shared.vo.GeoPoint destinationPoint, FlowDistanceKm averageDistanceKm, com.appbit.geoanalytics.domain.shared.vo.Period predominantPeriod) {
        return new OriginDestinationFlow(id, sourceId, originRegionId, destinationRegionId, "Cluster Norte", "Cluster Sur", "Cordoba", "Villa Maria", originPoint, destinationPoint, false, 20L, 35L, averageDistanceKm, predominantPeriod);
    }

    private OriginDestinationFlow flowWithCounters(long users, long trips) {
        return new OriginDestinationFlow(originDestinationFlowId(), dataSourceId(), regionId(), regionId(), "Cluster Norte", "Cluster Sur", "Cordoba", "Villa Maria", geoPoint(), destinationGeoPoint(), false, users, trips, flowDistanceKm("12.500"), period());
    }

    private OriginDestinationFlow flowWithTexts(String originClusterName, String destinationClusterName, String originMunicipality, String destinationMunicipality) {
        return new OriginDestinationFlow(originDestinationFlowId(), dataSourceId(), regionId(), regionId(), originClusterName, destinationClusterName, originMunicipality, destinationMunicipality, geoPoint(), destinationGeoPoint(), false, 20L, 35L, flowDistanceKm("12.500"), period());
    }

    @Nested
    class OriginDestinationFlowIdTest {
        @Test
        void shouldAcceptUuidV7() {
            OriginDestinationFlowId id = new OriginDestinationFlowId(uuidV7());
            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new OriginDestinationFlowId(null)).isInstanceOf(MobilityDomainException.class).hasMessage("Origin destination flow id cannot be null");
            assertThatThrownBy(() -> new OriginDestinationFlowId(nilUuid())).isInstanceOf(MobilityDomainException.class).hasMessage("Origin destination flow id cannot be nil UUID");
            assertThatThrownBy(() -> new OriginDestinationFlowId(uuidV4())).isInstanceOf(MobilityDomainException.class).hasMessage("Origin destination flow id must be UUIDv7");
            assertThatThrownBy(() -> new OriginDestinationFlowId(nonRfc4122UuidV7())).isInstanceOf(MobilityDomainException.class).hasMessage("Origin destination flow id must be RFC 4122 compatible");
        }
    }

    @Nested
    class FlowDistanceKmTest {
        @Test
        void shouldAcceptZeroAndPositiveValues() {
            assertThat(new FlowDistanceKm(decimal("0.000")).value()).isEqualByComparingTo("0");
            assertThat(new FlowDistanceKm(decimal("10.123")).value()).isEqualByComparingTo("10.123");
        }

        @Test
        void shouldRejectInvalidValue() {
            assertThatThrownBy(() -> new FlowDistanceKm(null)).isInstanceOf(MobilityDomainException.class).hasMessage("Flow distance km cannot be null");
            assertThatThrownBy(() -> new FlowDistanceKm(decimal("-0.001"))).isInstanceOf(MobilityDomainException.class).hasMessage("Flow distance km cannot be negative");
            assertThatThrownBy(() -> new FlowDistanceKm(decimal("10.1234"))).isInstanceOf(MobilityDomainException.class).hasMessage("Flow distance km scale cannot exceed 3 decimal places");
        }
    }
}
