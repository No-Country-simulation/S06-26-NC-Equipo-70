package com.appbit.geoanalytics.domain.network.model;

import com.appbit.geoanalytics.domain.exception.IdentityRestrictionException;
import com.appbit.geoanalytics.domain.network.enums.NetworkIndicatorType;
import com.appbit.geoanalytics.domain.network.exception.NetworkDomainException;
import com.appbit.geoanalytics.domain.network.vo.NetworkIndicatorId;
import com.appbit.geoanalytics.domain.shared.enums.ConfidenceLevel;
import com.appbit.geoanalytics.domain.shared.enums.GapLevel;
import com.appbit.geoanalytics.domain.shared.enums.IndicatorUnit;
import com.appbit.geoanalytics.domain.shared.enums.PeriodType;
import com.appbit.geoanalytics.domain.shared.exception.SharedDomainException;
import com.appbit.geoanalytics.domain.shared.vo.IndicatorScore;
import com.appbit.geoanalytics.domain.shared.vo.Period;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NetworkIndicatorTest {

    @Test
    void shouldCreateValidIndicatorAndTrimDescription() {
        NetworkIndicator indicator = new NetworkIndicator(
                networkIndicatorId(), regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX,
                indicatorScore("0.7500"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH,
                period(), "  Descripcion valida del indicador  "
        );

        assertThat(indicator.getDescription()).isEqualTo("Descripcion valida del indicador");
        assertThat(indicator.getIndicatorType()).isEqualTo(NetworkIndicatorType.CONNECTIVITY_INDEX);
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> indicatorWith(null, regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, period()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Network indicator id cannot be null");
        assertThatThrownBy(() -> indicatorWith(networkIndicatorId(), null, dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, period()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Region id cannot be null");
        assertThatThrownBy(() -> indicatorWith(networkIndicatorId(), regionId(), null, NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, period()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Data source id cannot be null");
        assertThatThrownBy(() -> indicatorWith(networkIndicatorId(), regionId(), dataSourceId(), null, indicatorScore("0.75"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, period()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Network indicator type cannot be null");
        assertThatThrownBy(() -> indicatorWith(networkIndicatorId(), regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, null, IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, period()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Indicator score cannot be null");
        assertThatThrownBy(() -> indicatorWith(networkIndicatorId(), regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), null, GapLevel.MEDIUM, ConfidenceLevel.HIGH, period()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Indicator unit cannot be null");
        assertThatThrownBy(() -> indicatorWith(networkIndicatorId(), regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), IndicatorUnit.SCORE, null, ConfidenceLevel.HIGH, period()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Gap level cannot be null");
        assertThatThrownBy(() -> indicatorWith(networkIndicatorId(), regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), IndicatorUnit.SCORE, GapLevel.MEDIUM, null, period()))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Confidence level cannot be null");
        assertThatThrownBy(() -> indicatorWith(networkIndicatorId(), regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, null))
                .isInstanceOf(NetworkDomainException.class).hasMessage("Period cannot be null");
    }

    @Test
    void shouldRejectInvalidDescription() {
        assertThatThrownBy(() -> indicatorWithDescription(null)).isInstanceOf(NetworkDomainException.class).hasMessage("Description cannot be null");
        assertThatThrownBy(() -> indicatorWithDescription("   ")).isInstanceOf(NetworkDomainException.class).hasMessage("Description cannot be blank");
        assertThatThrownBy(() -> indicatorWithDescription("abcd")).isInstanceOf(NetworkDomainException.class).hasMessage("Description length must be between 5 and 500 characters");
        assertThatThrownBy(() -> indicatorWithDescription(text(501))).isInstanceOf(NetworkDomainException.class).hasMessage("Description length must be between 5 and 500 characters");
        assertThatThrownBy(() -> indicatorWithDescription("Descripcion\ninvalida")).isInstanceOf(NetworkDomainException.class).hasMessage("Description cannot contain control characters");
    }

    @Test
    void shouldCompareIndicatorsByIdentity() {
        NetworkIndicatorId sameId = networkIndicatorId();
        NetworkIndicator first = indicatorWith(sameId, regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, period());
        NetworkIndicator second = indicatorWith(sameId, regionId(), dataSourceId(), NetworkIndicatorType.QUALITY_INDEX, indicatorScore("0.25"), IndicatorUnit.INDEX, GapLevel.HIGH, ConfidenceLevel.LOW, new Period(PeriodType.NOITE));

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(networkIndicator());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not an indicator");
    }

    private NetworkIndicator indicatorWith(NetworkIndicatorId id, com.appbit.geoanalytics.domain.region.vo.RegionId regionId, com.appbit.geoanalytics.domain.source.vo.DataSourceId sourceId, NetworkIndicatorType type, IndicatorScore score, IndicatorUnit unit, GapLevel gapLevel, ConfidenceLevel confidenceLevel, Period period) {
        return new NetworkIndicator(id, regionId, sourceId, type, score, unit, gapLevel, confidenceLevel, period, "Descripcion valida del indicador de red");
    }

    private NetworkIndicator indicatorWithDescription(String description) {
        return new NetworkIndicator(networkIndicatorId(), regionId(), dataSourceId(), NetworkIndicatorType.CONNECTIVITY_INDEX, indicatorScore("0.75"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, period(), description);
    }

    @Nested
    class NetworkIndicatorIdTest {
        @Test
        void shouldAcceptUuidV7() {
            NetworkIndicatorId id = new NetworkIndicatorId(uuidV7());
            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new NetworkIndicatorId(null)).isInstanceOf(IdentityRestrictionException.class).hasMessage("NetworkIndicatorId cannot be null");
            assertThatThrownBy(() -> new NetworkIndicatorId(nilUuid())).isInstanceOf(IdentityRestrictionException.class).hasMessage("NetworkIndicatorId cannot be nil UUID");
            assertThatThrownBy(() -> new NetworkIndicatorId(uuidV4())).isInstanceOf(IdentityRestrictionException.class).hasMessage("NetworkIndicatorId must be UUIDv7. Received version: 4");
            assertThatThrownBy(() -> new NetworkIndicatorId(nonRfc4122UuidV7())).isInstanceOf(IdentityRestrictionException.class).hasMessage("NetworkIndicatorId must use RFC 4122/IETF variant. Received variant: 0");
        }
    }

    @Nested
    class IndicatorScoreAndPeriodTest {
        @Test
        void shouldAcceptBoundaries() {
            assertThat(new IndicatorScore(decimal("0.0000")).value()).isEqualByComparingTo("0");
            assertThat(new IndicatorScore(decimal("1.0000")).value()).isEqualByComparingTo("1");
            assertThat(new Period(PeriodType.MANHA).value()).isEqualTo(PeriodType.MANHA);
        }

        @Test
        void shouldRejectInvalidScoreAndPeriod() {
            assertThatThrownBy(() -> new IndicatorScore(null)).isInstanceOf(SharedDomainException.class).hasMessage("Indicator score cannot be null");
            assertThatThrownBy(() -> new IndicatorScore(decimal("-0.0001"))).isInstanceOf(SharedDomainException.class).hasMessage("Indicator score must be between 0 and 1");
            assertThatThrownBy(() -> new IndicatorScore(decimal("1.0001"))).isInstanceOf(SharedDomainException.class).hasMessage("Indicator score must be between 0 and 1");
            assertThatThrownBy(() -> new IndicatorScore(decimal("0.12345"))).isInstanceOf(SharedDomainException.class).hasMessage("Indicator score scale cannot exceed 4 decimal places");
            assertThatThrownBy(() -> new Period(null)).isInstanceOf(SharedDomainException.class).hasMessage("Period cannot be null");
        }
    }
}
