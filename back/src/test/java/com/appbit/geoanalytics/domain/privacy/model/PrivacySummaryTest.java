package com.appbit.geoanalytics.domain.privacy.model;

import com.appbit.geoanalytics.domain.privacy.exception.PrivacyDomainException;
import com.appbit.geoanalytics.domain.privacy.vo.PrivacyParameter;
import com.appbit.geoanalytics.domain.privacy.vo.PrivacySummaryId;
import com.appbit.geoanalytics.domain.privacy.vo.PrivacyValue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PrivacySummaryTest {

    @Test
    void shouldCreateValidSummary() {
        PrivacySummary summary = privacySummary();

        assertThat(summary.getId()).isNotNull();
        assertThat(summary.getSourceId()).isNotNull();
        assertThat(summary.getParameter()).isEqualTo(new PrivacyParameter("k_anonymity"));
        assertThat(summary.getValue()).isEqualTo(new PrivacyValue("10"));
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> new PrivacySummary(null, dataSourceId(), privacyParameter(), privacyValue()))
                .isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy summary id cannot be null");
        assertThatThrownBy(() -> new PrivacySummary(privacySummaryId(), null, privacyParameter(), privacyValue()))
                .isInstanceOf(PrivacyDomainException.class).hasMessage("Data source id cannot be null");
        assertThatThrownBy(() -> new PrivacySummary(privacySummaryId(), dataSourceId(), null, privacyValue()))
                .isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy parameter cannot be null");
        assertThatThrownBy(() -> new PrivacySummary(privacySummaryId(), dataSourceId(), privacyParameter(), null))
                .isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy value cannot be null");
    }

    @Test
    void shouldCompareSummariesByIdentity() {
        PrivacySummaryId sameId = privacySummaryId();
        PrivacySummary first = new PrivacySummary(sameId, dataSourceId(), privacyParameter(), privacyValue());
        PrivacySummary second = new PrivacySummary(sameId, dataSourceId(), new PrivacyParameter("epsilon"), new PrivacyValue("0.5"));

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(privacySummary());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not a summary");
    }

    @Nested
    class PrivacySummaryIdTest {
        @Test
        void shouldAcceptUuidV7() {
            PrivacySummaryId id = new PrivacySummaryId(uuidV7());
            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new PrivacySummaryId(null)).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy summary id cannot be null");
            assertThatThrownBy(() -> new PrivacySummaryId(nilUuid())).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy summary id cannot be nil UUID");
            assertThatThrownBy(() -> new PrivacySummaryId(uuidV4())).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy summary id must be UUIDv7");
            assertThatThrownBy(() -> new PrivacySummaryId(nonRfc4122UuidV7())).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy summary id must be RFC 4122 compatible");
        }
    }

    @Nested
    class PrivacyParameterTest {
        @Test
        void shouldTrimValidParameter() {
            assertThat(new PrivacyParameter("  epsilon  ").value()).isEqualTo("epsilon");
        }

        @Test
        void shouldRejectInvalidParameter() {
            assertThatThrownBy(() -> new PrivacyParameter(null)).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy parameter cannot be null");
            assertThatThrownBy(() -> new PrivacyParameter("   ")).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy parameter cannot be blank");
            assertThatThrownBy(() -> new PrivacyParameter("a")).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy parameter length must be between 2 and 80 characters");
            assertThatThrownBy(() -> new PrivacyParameter(text(81))).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy parameter length must be between 2 and 80 characters");
            assertThatThrownBy(() -> new PrivacyParameter("eps\nilon")).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy parameter cannot contain control characters");
        }
    }

    @Nested
    class PrivacyValueTest {
        @Test
        void shouldTrimValidValue() {
            assertThat(new PrivacyValue("  10  ").value()).isEqualTo("10");
        }

        @Test
        void shouldRejectInvalidValue() {
            assertThatThrownBy(() -> new PrivacyValue(null)).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy value cannot be null");
            assertThatThrownBy(() -> new PrivacyValue("   ")).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy value cannot be blank");
            assertThatThrownBy(() -> new PrivacyValue(text(201))).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy value length must be between 1 and 200 characters");
            assertThatThrownBy(() -> new PrivacyValue("1\n0")).isInstanceOf(PrivacyDomainException.class).hasMessage("Privacy value cannot contain control characters");
        }
    }
}
