package com.appbit.geoanalytics.domain.network.model;

import com.appbit.geoanalytics.domain.exception.IdentityRestrictionException;
import com.appbit.geoanalytics.domain.network.exception.NetworkDomainException;
import com.appbit.geoanalytics.domain.network.vo.AntennaId;
import com.appbit.geoanalytics.domain.shared.exception.SharedDomainException;
import com.appbit.geoanalytics.domain.shared.vo.Ecgi;
import com.appbit.geoanalytics.domain.shared.vo.GeoPoint;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AntennaTest {

    @Test
    void shouldCreateValidAntennaAndTrimTexts() {
        Antenna antenna = new Antenna(
                antennaId(), originEcgi(), regionId(), "  Cluster Centro  ", "  Cordoba  ", geoPoint(), dataSourceId()
        );

        assertThat(antenna.getClusterName()).isEqualTo("Cluster Centro");
        assertThat(antenna.getMunicipality()).isEqualTo("Cordoba");
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> antennaWith(null, originEcgi(), regionId(), geoPoint(), dataSourceId()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Antenna id cannot be null");
        assertThatThrownBy(() -> antennaWith(antennaId(), null, regionId(), geoPoint(), dataSourceId()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("ECGI cannot be null");
        assertThatThrownBy(() -> antennaWith(antennaId(), originEcgi(), null, geoPoint(), dataSourceId()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Region id cannot be null");
        assertThatThrownBy(() -> antennaWith(antennaId(), originEcgi(), regionId(), null, dataSourceId()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Antenna location cannot be null");
        assertThatThrownBy(() -> antennaWith(antennaId(), originEcgi(), regionId(), geoPoint(), null))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Data source id cannot be null");
    }

    @Test
    void shouldRejectInvalidTexts() {
        assertThatThrownBy(() -> antennaWithTexts(null, "Cordoba")).isInstanceOf(NetworkDomainException.class).hasMessage("Cluster name cannot be null");
        assertThatThrownBy(() -> antennaWithTexts(" ", "Cordoba")).isInstanceOf(NetworkDomainException.class).hasMessage("Cluster name cannot be blank");
        assertThatThrownBy(() -> antennaWithTexts("A", "Cordoba")).isInstanceOf(NetworkDomainException.class).hasMessage("Cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> antennaWithTexts(text(41), "Cordoba")).isInstanceOf(NetworkDomainException.class).hasMessage("Cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> antennaWithTexts("Cluster\nCentro", "Cordoba")).isInstanceOf(NetworkDomainException.class).hasMessage("Cluster name cannot contain control characters");
        assertThatThrownBy(() -> antennaWithTexts("Cluster Centro", null)).isInstanceOf(NetworkDomainException.class).hasMessage("Municipality cannot be null");
        assertThatThrownBy(() -> antennaWithTexts("Cluster Centro", " ")).isInstanceOf(NetworkDomainException.class).hasMessage("Municipality cannot be blank");
        assertThatThrownBy(() -> antennaWithTexts("Cluster Centro", "A")).isInstanceOf(NetworkDomainException.class).hasMessage("Municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> antennaWithTexts("Cluster Centro", text(61))).isInstanceOf(NetworkDomainException.class).hasMessage("Municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> antennaWithTexts("Cluster Centro", "Cor\ndoba")).isInstanceOf(NetworkDomainException.class).hasMessage("Municipality cannot contain control characters");
    }

    @Test
    void shouldCompareAntennasByIdentity() {
        AntennaId sameId = antennaId();
        Antenna first = antennaWith(sameId, originEcgi(), regionId(), geoPoint(), dataSourceId());
        Antenna second = antennaWith(sameId, destinationEcgi(), regionId(), destinationGeoPoint(), dataSourceId());

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(antenna());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not an antenna");
    }

    private Antenna antennaWith(AntennaId id, Ecgi ecgi, com.appbit.geoanalytics.domain.region.vo.RegionId regionId, GeoPoint location, com.appbit.geoanalytics.domain.source.vo.DataSourceId sourceId) {
        return new Antenna(id, ecgi, regionId, "Cluster Centro", "Cordoba", location, sourceId);
    }

    private Antenna antennaWithTexts(String clusterName, String municipality) {
        return new Antenna(antennaId(), originEcgi(), regionId(), clusterName, municipality, geoPoint(), dataSourceId());
    }

    @Nested
    class AntennaIdTest {
        @Test
        void shouldAcceptUuidV7() {
            AntennaId id = new AntennaId(uuidV7());
            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new AntennaId(null)).isInstanceOf(IdentityRestrictionException.class).hasMessage("AntennaId cannot be null");
            assertThatThrownBy(() -> new AntennaId(nilUuid())).isInstanceOf(IdentityRestrictionException.class).hasMessage("AntennaId cannot be nil UUID");
            assertThatThrownBy(() -> new AntennaId(uuidV4())).isInstanceOf(IdentityRestrictionException.class).hasMessage("AntennaId must be UUIDv7. Received version: 4");
            assertThatThrownBy(() -> new AntennaId(nonRfc4122UuidV7())).isInstanceOf(IdentityRestrictionException.class).hasMessage("AntennaId must use RFC 4122/IETF variant. Received variant: 0");
        }
    }

    @Nested
    class EcgiTest {
        @Test
        void shouldTrimValidEcgi() {
            assertThat(new Ecgi("  123456789012  ").value()).isEqualTo("123456789012");
        }

        @Test
        void shouldRejectInvalidEcgi() {
            assertThatThrownBy(() -> new Ecgi(null)).isInstanceOf(SharedDomainException.class).hasMessage("ECGI cannot be null");
            assertThatThrownBy(() -> new Ecgi("   ")).isInstanceOf(SharedDomainException.class).hasMessage("ECGI cannot be blank");
            assertThatThrownBy(() -> new Ecgi("12345678901")).isInstanceOf(SharedDomainException.class).hasMessage("ECGI length must be between 12 and 16 characters");
            assertThatThrownBy(() -> new Ecgi("12345678901234567")).isInstanceOf(SharedDomainException.class).hasMessage("ECGI length must be between 12 and 16 characters");
            assertThatThrownBy(() -> new Ecgi("12345678901A")).isInstanceOf(SharedDomainException.class).hasMessage("ECGI must contain only digits");
            assertThatThrownBy(() -> new Ecgi("123456 89012")).isInstanceOf(SharedDomainException.class).hasMessage("ECGI must contain only digits");
            assertThatThrownBy(() -> new Ecgi("123456\n789012")).isInstanceOf(SharedDomainException.class).hasMessage("ECGI must contain only digits");
        }
    }

    @Nested
    class GeoPointTest {
        @Test
        void shouldNormalizeCoordinates() {
            assertThat(new GeoPoint(decimal("90.000000"), decimal("58.000000")).longitude()).isEqualByComparingTo("0.000000");
            assertThat(new GeoPoint(decimal("10.000000"), decimal("180.000000")).longitude()).isEqualByComparingTo("-180.000000");
        }

        @Test
        void shouldRejectInvalidCoordinates() {
            assertThatThrownBy(() -> new GeoPoint(null, decimal("0.000000"))).isInstanceOf(SharedDomainException.class).hasMessage("Latitude cannot be null");
            assertThatThrownBy(() -> new GeoPoint(decimal("0.000000"), null)).isInstanceOf(SharedDomainException.class).hasMessage("Longitude cannot be null");
            assertThatThrownBy(() -> new GeoPoint(decimal("90.000001"), decimal("0.000000"))).isInstanceOf(SharedDomainException.class).hasMessage("Latitude must be between -90 and 90 degrees");
            assertThatThrownBy(() -> new GeoPoint(decimal("0.000000"), decimal("180.000001"))).isInstanceOf(SharedDomainException.class).hasMessage("Longitude must be between -180 and 180 degrees");
            assertThatThrownBy(() -> new GeoPoint(decimal("0.0000001"), decimal("0.000000"))).isInstanceOf(SharedDomainException.class).hasMessage("Latitude must have at most 6 decimal places");
            assertThatThrownBy(() -> new GeoPoint(decimal("0.000000"), decimal("0.0000001"))).isInstanceOf(SharedDomainException.class).hasMessage("Longitude must have at most 6 decimal places");
        }
    }
}
