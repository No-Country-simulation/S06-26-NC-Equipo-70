package com.appbit.geoanalytics.domain.region.model;

import com.appbit.geoanalytics.domain.exception.IdentityRestrictionException;
import com.appbit.geoanalytics.domain.region.exception.MissingRegionCodeException;
import com.appbit.geoanalytics.domain.region.exception.RegionDomainException;
import com.appbit.geoanalytics.domain.region.vo.RegionCode;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegionTest {

    @Test
    void shouldCreateValidRegionAndTrimTexts() {
        Region region = new Region(regionId(), new RegionCode(" ar-cba_001 "), "  Cluster Centro  ", "  Nueva Cordoba  ", "  Cordoba  ", geoPoint());

        assertThat(region.getCode().value()).isEqualTo("AR-CBA_001");
        assertThat(region.getClusterName()).isEqualTo("Cluster Centro");
        assertThat(region.getName()).isEqualTo("Nueva Cordoba");
        assertThat(region.getMunicipality()).isEqualTo("Cordoba");
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> new Region(null, regionCode(), "Cluster Centro", "Nueva Cordoba", "Cordoba", geoPoint()))
                .isInstanceOf(RegionDomainException.class).hasMessage("Region id cannot be null");
        assertThatThrownBy(() -> new Region(regionId(), null, "Cluster Centro", "Nueva Cordoba", "Cordoba", geoPoint()))
                .isInstanceOf(RegionDomainException.class).hasMessage("Region code cannot be null");
        assertThatThrownBy(() -> new Region(regionId(), regionCode(), "Cluster Centro", "Nueva Cordoba", "Cordoba", null))
                .isInstanceOf(RegionDomainException.class).hasMessage("Region coordinates cannot be null");
    }

    @Test
    void shouldRejectInvalidTexts() {
        assertThatThrownBy(() -> regionWithTexts("Cluster Centro", null, "Cordoba")).isInstanceOf(RegionDomainException.class).hasMessage("Region name cannot be null");
        assertThatThrownBy(() -> regionWithTexts("Cluster Centro", " ", "Cordoba")).isInstanceOf(RegionDomainException.class).hasMessage("Region name cannot be blank");
        assertThatThrownBy(() -> regionWithTexts("Cluster Centro", "A", "Cordoba")).isInstanceOf(RegionDomainException.class).hasMessage("Region name length must be between 2 and 80 characters");
        assertThatThrownBy(() -> regionWithTexts("Cluster Centro", text(81), "Cordoba")).isInstanceOf(RegionDomainException.class).hasMessage("Region name length must be between 2 and 80 characters");

        assertThatThrownBy(() -> regionWithTexts("Cluster Centro", "Nueva Cordoba", null)).isInstanceOf(RegionDomainException.class).hasMessage("Municipality cannot be null");
        assertThatThrownBy(() -> regionWithTexts("Cluster Centro", "Nueva Cordoba", " ")).isInstanceOf(RegionDomainException.class).hasMessage("Municipality cannot be blank");
        assertThatThrownBy(() -> regionWithTexts("Cluster Centro", "Nueva Cordoba", "A")).isInstanceOf(RegionDomainException.class).hasMessage("Municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> regionWithTexts("Cluster Centro", "Nueva Cordoba", text(61))).isInstanceOf(RegionDomainException.class).hasMessage("Municipality length must be between 2 and 60 characters");

        assertThatThrownBy(() -> regionWithTexts(null, "Nueva Cordoba", "Cordoba")).isInstanceOf(RegionDomainException.class).hasMessage("ClusterName cannot be null");
        assertThatThrownBy(() -> regionWithTexts(" ", "Nueva Cordoba", "Cordoba")).isInstanceOf(RegionDomainException.class).hasMessage("ClusterName cannot be blank");
        assertThatThrownBy(() -> regionWithTexts("A", "Nueva Cordoba", "Cordoba")).isInstanceOf(RegionDomainException.class).hasMessage("ClusterName length must be between 2 and 40 characters");
        assertThatThrownBy(() -> regionWithTexts(text(41), "Nueva Cordoba", "Cordoba")).isInstanceOf(RegionDomainException.class).hasMessage("ClusterName length must be between 2 and 40 characters");
    }

    @Test
    void shouldCompareRegionsByIdentity() {
        RegionId sameId = regionId();
        Region first = new Region(sameId, regionCode(), "Cluster Centro", "Nueva Cordoba", "Cordoba", geoPoint());
        Region second = new Region(sameId, new RegionCode("AR-CBA-002"), "Cluster Norte", "Alta Cordoba", "Cordoba", destinationGeoPoint());

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(region());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not a region");
    }

    private Region regionWithTexts(String clusterName, String name, String municipality) {
        return new Region(regionId(), regionCode(), clusterName, name, municipality, geoPoint());
    }

    @Nested
    class RegionCodeTest {
        @Test
        void shouldNormalizeValidCode() {
            assertThat(new RegionCode(" ar-cba_001 ").value()).isEqualTo("AR-CBA_001");
        }

        @Test
        void shouldRejectMissingCode() {
            assertThatThrownBy(() -> new RegionCode(null)).isInstanceOf(MissingRegionCodeException.class).hasMessage("RegionCode cannot be null");
            assertThatThrownBy(() -> new RegionCode("   ")).isInstanceOf(MissingRegionCodeException.class).hasMessage("RegionCode cannot be blank");
        }

        @Test
        void shouldRejectInvalidCodeFormat() {
            assertThatThrownBy(() -> new RegionCode("AB")).isInstanceOf(RegionDomainException.class).hasMessage("RegionCode length must be between 3 and 80 characters");
            assertThatThrownBy(() -> new RegionCode(text(81))).isInstanceOf(RegionDomainException.class).hasMessage("RegionCode length must be between 3 and 80 characters");
            assertThatThrownBy(() -> new RegionCode("-ABC")).isInstanceOf(RegionDomainException.class).hasMessage("RegionCode must start with an uppercase letter or digit");
            assertThatThrownBy(() -> new RegionCode("ABC-")).isInstanceOf(RegionDomainException.class).hasMessage("RegionCode must end with an uppercase letter or digit");
            assertThatThrownBy(() -> new RegionCode("AB--C")).isInstanceOf(RegionDomainException.class).hasMessage("RegionCode cannot contain consecutive separators");
            assertThatThrownBy(() -> new RegionCode("AB.C")).isInstanceOf(RegionDomainException.class).hasMessage("RegionCode can only contain uppercase letters, digits, hyphen or underscore");
        }
    }

    @Nested
    class RegionIdTest {
        @Test
        void shouldAcceptUuidV7() {
            RegionId id = new RegionId(uuidV7());
            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new RegionId(null)).isInstanceOf(IdentityRestrictionException.class).hasMessage("RegionId cannot be null.");
            assertThatThrownBy(() -> new RegionId(nilUuid())).isInstanceOf(IdentityRestrictionException.class).hasMessage("RegionId cannot be the zero UUID: 00000000-0000-0000-0000-000000000000.");
            assertThatThrownBy(() -> new RegionId(uuidV4())).isInstanceOf(IdentityRestrictionException.class).hasMessage("RegionId must be UUIDv7. Received value: version 4.");
            assertThatThrownBy(() -> new RegionId(nonRfc4122UuidV7())).isInstanceOf(IdentityRestrictionException.class).hasMessage("RegionId must use RFC 4122/IETF variant. Received value: variant 0.");
        }
    }
}
