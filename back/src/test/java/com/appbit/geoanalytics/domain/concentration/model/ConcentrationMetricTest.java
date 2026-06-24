package com.appbit.geoanalytics.domain.concentration.model;

import com.appbit.geoanalytics.domain.concentration.exception.ConcentrationDomainException;
import com.appbit.geoanalytics.domain.concentration.vo.ConcentrationMetricId;
import com.appbit.geoanalytics.domain.concentration.vo.MetricRatio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConcentrationMetricTest {

    @Test
    void shouldCreateValidMetricAndTrimTexts() {
        ConcentrationMetric metric = new ConcentrationMetric(
                concentrationMetricId(),
                dataSourceId(),
                regionId(),
                originEcgi(),
                "  Cluster Centro  ",
                "  Cordoba  ",
                LocalDate.of(2026, 1, 15),
                period(),
                100L,
                180L,
                1_000L,
                500L,
                120,
                metricRatio("0.1250"),
                metricRatio("0.2500"),
                60,
                25,
                geoPoint()
        );

        assertThat(metric.getClusterName()).isEqualTo("Cluster Centro");
        assertThat(metric.getMunicipality()).isEqualTo("Cordoba");
        assertThat(metric.getActiveUsers()).isEqualTo(100L);
        assertThat(metric.getAverageDropRate()).isEqualTo(metricRatio("0.1250"));
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> metricWith(null, dataSourceId(), regionId(), originEcgi(), LocalDate.now(), period(), metricRatio("0.1"), metricRatio("0.2"), geoPoint()))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Concentration metric id cannot be null");
        assertThatThrownBy(() -> metricWith(concentrationMetricId(), null, regionId(), originEcgi(), LocalDate.now(), period(), metricRatio("0.1"), metricRatio("0.2"), geoPoint()))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Data source id cannot be null");
        assertThatThrownBy(() -> metricWith(concentrationMetricId(), dataSourceId(), null, originEcgi(), LocalDate.now(), period(), metricRatio("0.1"), metricRatio("0.2"), geoPoint()))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Region id cannot be null");
        assertThatThrownBy(() -> metricWith(concentrationMetricId(), dataSourceId(), regionId(), null, LocalDate.now(), period(), metricRatio("0.1"), metricRatio("0.2"), geoPoint()))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("ECGI cannot be null");
        assertThatThrownBy(() -> metricWith(concentrationMetricId(), dataSourceId(), regionId(), originEcgi(), null, period(), metricRatio("0.1"), metricRatio("0.2"), geoPoint()))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Day date cannot be null");
        assertThatThrownBy(() -> metricWith(concentrationMetricId(), dataSourceId(), regionId(), originEcgi(), LocalDate.now(), null, metricRatio("0.1"), metricRatio("0.2"), geoPoint()))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Period cannot be null");
        assertThatThrownBy(() -> metricWith(concentrationMetricId(), dataSourceId(), regionId(), originEcgi(), LocalDate.now(), period(), null, metricRatio("0.2"), geoPoint()))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Average drop rate cannot be null");
        assertThatThrownBy(() -> metricWith(concentrationMetricId(), dataSourceId(), regionId(), originEcgi(), LocalDate.now(), period(), metricRatio("0.1"), null, geoPoint()))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Average congestion cannot be null");
        assertThatThrownBy(() -> metricWith(concentrationMetricId(), dataSourceId(), regionId(), originEcgi(), LocalDate.now(), period(), metricRatio("0.1"), metricRatio("0.2"), null))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Location cannot be null");
    }

    @Test
    void shouldRejectInvalidTexts() {
        assertThatThrownBy(() -> metricWithTexts(null, "Cordoba"))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Cluster name cannot be null");
        assertThatThrownBy(() -> metricWithTexts("   ", "Cordoba"))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Cluster name cannot be blank");
        assertThatThrownBy(() -> metricWithTexts("A", "Cordoba"))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> metricWithTexts(text(41), "Cordoba"))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Cluster name length must be between 2 and 40 characters");
        assertThatThrownBy(() -> metricWithTexts("Cluster\nCentro", "Cordoba"))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Cluster name cannot contain control characters");

        assertThatThrownBy(() -> metricWithTexts("Cluster Centro", null))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Municipality cannot be null");
        assertThatThrownBy(() -> metricWithTexts("Cluster Centro", "   "))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Municipality cannot be blank");
        assertThatThrownBy(() -> metricWithTexts("Cluster Centro", "A"))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> metricWithTexts("Cluster Centro", text(61)))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Municipality length must be between 2 and 60 characters");
        assertThatThrownBy(() -> metricWithTexts("Cluster Centro", "Cor\ndoba"))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Municipality cannot contain control characters");
    }

    @Test
    void shouldRejectNegativeCounters() {
        assertThatThrownBy(() -> metricWithCounters(-1L, 1L, 1L, 1L, 1, 1, 1))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Active users cannot be negative");
        assertThatThrownBy(() -> metricWithCounters(1L, -1L, 1L, 1L, 1, 1, 1))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Sessions cannot be negative");
        assertThatThrownBy(() -> metricWithCounters(1L, 1L, -1L, 1L, 1, 1, 1))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Download bytes cannot be negative");
        assertThatThrownBy(() -> metricWithCounters(1L, 1L, 1L, -1L, 1, 1, 1))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Upload bytes cannot be negative");
        assertThatThrownBy(() -> metricWithCounters(1L, 1L, 1L, 1L, -1, 1, 1))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Average session duration seconds cannot be negative");
        assertThatThrownBy(() -> metricWithCounters(1L, 1L, 1L, 1L, 1, -1, 1))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Total calls cannot be negative");
        assertThatThrownBy(() -> metricWithCounters(1L, 1L, 1L, 1L, 1, 1, -1))
                .isInstanceOf(ConcentrationDomainException.class).hasMessage("Total messages cannot be negative");
    }

    @Test
    void shouldCompareMetricsByIdentity() {
        ConcentrationMetricId sameId = concentrationMetricId();
        ConcentrationMetric first = metricWith(sameId, dataSourceId(), regionId(), originEcgi(), LocalDate.now(), period(), metricRatio("0.1"), metricRatio("0.2"), geoPoint());
        ConcentrationMetric second = metricWith(sameId, dataSourceId(), regionId(), originEcgi(), LocalDate.now().minusDays(1), period(), metricRatio("0.3"), metricRatio("0.4"), geoPoint());

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(concentrationMetric());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not a metric");
    }

    private ConcentrationMetric metricWith(
            ConcentrationMetricId id,
            com.appbit.geoanalytics.domain.source.vo.DataSourceId sourceId,
            com.appbit.geoanalytics.domain.region.vo.RegionId regionId,
            com.appbit.geoanalytics.domain.shared.vo.Ecgi ecgi,
            LocalDate dayDate,
            com.appbit.geoanalytics.domain.shared.vo.Period period,
            MetricRatio dropRate,
            MetricRatio congestion,
            com.appbit.geoanalytics.domain.shared.vo.GeoPoint location
    ) {
        return new ConcentrationMetric(id, sourceId, regionId, ecgi, "Cluster Centro", "Cordoba", dayDate, period, 100L, 180L, 1_000L, 500L, 120, dropRate, congestion, 60, 25, location);
    }

    private ConcentrationMetric metricWithTexts(String clusterName, String municipality) {
        return new ConcentrationMetric(concentrationMetricId(), dataSourceId(), regionId(), originEcgi(), clusterName, municipality, LocalDate.now(), period(), 100L, 180L, 1_000L, 500L, 120, metricRatio("0.1"), metricRatio("0.2"), 60, 25, geoPoint());
    }

    private ConcentrationMetric metricWithCounters(long activeUsers, long sessions, long downloadBytes, long uploadBytes, int averageDuration, int totalCalls, int totalMessages) {
        return new ConcentrationMetric(concentrationMetricId(), dataSourceId(), regionId(), originEcgi(), "Cluster Centro", "Cordoba", LocalDate.now(), period(), activeUsers, sessions, downloadBytes, uploadBytes, averageDuration, metricRatio("0.1"), metricRatio("0.2"), totalCalls, totalMessages, geoPoint());
    }

    @Nested
    class ConcentrationMetricIdTest {

        @Test
        void shouldAcceptUuidV7() {
            ConcentrationMetricId id = new ConcentrationMetricId(uuidV7());

            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new ConcentrationMetricId(null)).isInstanceOf(ConcentrationDomainException.class).hasMessage("Concentration metric id cannot be null");
            assertThatThrownBy(() -> new ConcentrationMetricId(nilUuid())).isInstanceOf(ConcentrationDomainException.class).hasMessage("Concentration metric id cannot be nil UUID");
            assertThatThrownBy(() -> new ConcentrationMetricId(uuidV4())).isInstanceOf(ConcentrationDomainException.class).hasMessage("Concentration metric id must be UUIDv7");
            assertThatThrownBy(() -> new ConcentrationMetricId(nonRfc4122UuidV7())).isInstanceOf(ConcentrationDomainException.class).hasMessage("Concentration metric id must be RFC 4122 compatible");
        }
    }

    @Nested
    class MetricRatioTest {

        @Test
        void shouldAcceptBoundaries() {
            assertThat(new MetricRatio(BigDecimal.ZERO).value()).isEqualByComparingTo("0");
            assertThat(new MetricRatio(BigDecimal.ONE).value()).isEqualByComparingTo("1");
            assertThat(new MetricRatio(decimal("0.1234")).value()).isEqualByComparingTo("0.1234");
        }

        @Test
        void shouldRejectInvalidValue() {
            assertThatThrownBy(() -> new MetricRatio(null)).isInstanceOf(ConcentrationDomainException.class).hasMessage("Metric ratio cannot be null");
            assertThatThrownBy(() -> new MetricRatio(decimal("-0.0001"))).isInstanceOf(ConcentrationDomainException.class).hasMessage("Metric ratio must be between 0 and 1");
            assertThatThrownBy(() -> new MetricRatio(decimal("1.0001"))).isInstanceOf(ConcentrationDomainException.class).hasMessage("Metric ratio must be between 0 and 1");
            assertThatThrownBy(() -> new MetricRatio(decimal("0.12345"))).isInstanceOf(ConcentrationDomainException.class).hasMessage("Metric ratio scale cannot exceed 4 decimal places");
        }
    }
}
