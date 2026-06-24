package com.appbit.geoanalytics.domain.social.model;

import com.appbit.geoanalytics.domain.shared.enums.ConfidenceLevel;
import com.appbit.geoanalytics.domain.shared.enums.GapLevel;
import com.appbit.geoanalytics.domain.shared.enums.IndicatorUnit;
import com.appbit.geoanalytics.domain.social.enums.SocialIndicatorType;
import com.appbit.geoanalytics.domain.social.exception.SocialDomainException;
import com.appbit.geoanalytics.domain.social.vo.SocialIndicatorId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SocialIndicatorTest {

    @Test
    void shouldCreateValidIndicatorAndTrimDescription() {
        SocialIndicator indicator = new SocialIndicator(
                socialIndicatorId(), regionId(), dataSourceId(), SocialIndicatorType.TRAINING,
                indicatorScore("0.6500"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH,
                "  Descripcion valida del indicador social  "
        );

        assertThat(indicator.getDescription()).isEqualTo("Descripcion valida del indicador social");
        assertThat(indicator.getIndicatorType()).isEqualTo(SocialIndicatorType.TRAINING);
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> indicatorWith(null, regionId(), dataSourceId(), SocialIndicatorType.TRAINING, indicatorScore("0.65"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH))
                .isInstanceOf(SocialDomainException.class).hasMessage("Social indicator id cannot be null");
        assertThatThrownBy(() -> indicatorWith(socialIndicatorId(), null, dataSourceId(), SocialIndicatorType.TRAINING, indicatorScore("0.65"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH))
                .isInstanceOf(SocialDomainException.class).hasMessage("Region id cannot be null");
        assertThatThrownBy(() -> indicatorWith(socialIndicatorId(), regionId(), null, SocialIndicatorType.TRAINING, indicatorScore("0.65"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH))
                .isInstanceOf(SocialDomainException.class).hasMessage("Data source id cannot be null");
        assertThatThrownBy(() -> indicatorWith(socialIndicatorId(), regionId(), dataSourceId(), null, indicatorScore("0.65"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH))
                .isInstanceOf(SocialDomainException.class).hasMessage("Social indicator type cannot be null");
        assertThatThrownBy(() -> indicatorWith(socialIndicatorId(), regionId(), dataSourceId(), SocialIndicatorType.TRAINING, null, IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH))
                .isInstanceOf(SocialDomainException.class).hasMessage("Indicator score cannot be null");
        assertThatThrownBy(() -> indicatorWith(socialIndicatorId(), regionId(), dataSourceId(), SocialIndicatorType.TRAINING, indicatorScore("0.65"), null, GapLevel.MEDIUM, ConfidenceLevel.HIGH))
                .isInstanceOf(SocialDomainException.class).hasMessage("Indicator unit cannot be null");
        assertThatThrownBy(() -> indicatorWith(socialIndicatorId(), regionId(), dataSourceId(), SocialIndicatorType.TRAINING, indicatorScore("0.65"), IndicatorUnit.SCORE, null, ConfidenceLevel.HIGH))
                .isInstanceOf(SocialDomainException.class).hasMessage("Gap level cannot be null");
        assertThatThrownBy(() -> indicatorWith(socialIndicatorId(), regionId(), dataSourceId(), SocialIndicatorType.TRAINING, indicatorScore("0.65"), IndicatorUnit.SCORE, GapLevel.MEDIUM, null))
                .isInstanceOf(SocialDomainException.class).hasMessage("Confidence level cannot be null");
    }

    @Test
    void shouldRejectInvalidDescription() {
        assertThatThrownBy(() -> indicatorWithDescription(null)).isInstanceOf(SocialDomainException.class).hasMessage("Description cannot be null");
        assertThatThrownBy(() -> indicatorWithDescription("   ")).isInstanceOf(SocialDomainException.class).hasMessage("Description cannot be blank");
        assertThatThrownBy(() -> indicatorWithDescription("abcd")).isInstanceOf(SocialDomainException.class).hasMessage("Description length must be between 5 and 500 characters");
        assertThatThrownBy(() -> indicatorWithDescription(text(501))).isInstanceOf(SocialDomainException.class).hasMessage("Description length must be between 5 and 500 characters");
        assertThatThrownBy(() -> indicatorWithDescription("Descripcion\ninvalida")).isInstanceOf(SocialDomainException.class).hasMessage("Description cannot contain control characters");
    }

    @Test
    void shouldCompareIndicatorsByIdentity() {
        SocialIndicatorId sameId = socialIndicatorId();
        SocialIndicator first = indicatorWith(sameId, regionId(), dataSourceId(), SocialIndicatorType.TRAINING, indicatorScore("0.65"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH);
        SocialIndicator second = indicatorWith(sameId, regionId(), dataSourceId(), SocialIndicatorType.MENTORSHIP, indicatorScore("0.25"), IndicatorUnit.INDEX, GapLevel.HIGH, ConfidenceLevel.LOW);

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(socialIndicator());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not an indicator");
    }

    private SocialIndicator indicatorWith(SocialIndicatorId id, com.appbit.geoanalytics.domain.region.vo.RegionId regionId, com.appbit.geoanalytics.domain.source.vo.DataSourceId sourceId, SocialIndicatorType type, com.appbit.geoanalytics.domain.shared.vo.IndicatorScore score, IndicatorUnit unit, GapLevel gapLevel, ConfidenceLevel confidenceLevel) {
        return new SocialIndicator(id, regionId, sourceId, type, score, unit, gapLevel, confidenceLevel, "Descripcion valida del indicador social");
    }

    private SocialIndicator indicatorWithDescription(String description) {
        return new SocialIndicator(socialIndicatorId(), regionId(), dataSourceId(), SocialIndicatorType.TRAINING, indicatorScore("0.65"), IndicatorUnit.SCORE, GapLevel.MEDIUM, ConfidenceLevel.HIGH, description);
    }

    @Nested
    class SocialIndicatorIdTest {
        @Test
        void shouldAcceptUuidV7() {
            SocialIndicatorId id = new SocialIndicatorId(uuidV7());
            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new SocialIndicatorId(null)).isInstanceOf(SocialDomainException.class).hasMessage("Social indicator id cannot be null");
            assertThatThrownBy(() -> new SocialIndicatorId(nilUuid())).isInstanceOf(SocialDomainException.class).hasMessage("Social indicator id cannot be nil UUID");
            assertThatThrownBy(() -> new SocialIndicatorId(uuidV4())).isInstanceOf(SocialDomainException.class).hasMessage("Social indicator id must be UUIDv7");
            assertThatThrownBy(() -> new SocialIndicatorId(nonRfc4122UuidV7())).isInstanceOf(SocialDomainException.class).hasMessage("Social indicator id must be RFC 4122 compatible");
        }
    }
}
