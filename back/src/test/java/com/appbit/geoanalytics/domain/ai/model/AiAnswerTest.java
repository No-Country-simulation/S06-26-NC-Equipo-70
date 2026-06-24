package com.appbit.geoanalytics.domain.ai.model;

import com.appbit.geoanalytics.domain.ai.enums.VisualizationType;
import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;
import com.appbit.geoanalytics.domain.ai.vo.AiAnswerId;
import com.appbit.geoanalytics.domain.ai.vo.AnswerEvidence;
import com.appbit.geoanalytics.domain.ai.vo.AnswerExplanation;
import com.appbit.geoanalytics.domain.ai.vo.AnswerSummary;
import com.appbit.geoanalytics.domain.ai.vo.AnswerWarning;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.enums.ConfidenceLevel;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AiAnswerTest {

    @Test
    void shouldCreateHighConfidenceAnswerWithEvidenceAndSources() {
        List<AnswerEvidence> evidence = new ArrayList<>(List.of(answerEvidence()));
        List<RegionId> regionIds = new ArrayList<>(List.of(regionId()));
        List<DataSourceId> sourceIds = new ArrayList<>(List.of(dataSourceId()));

        AiAnswer answer = new AiAnswer(
                aiAnswerId(),
                aiQueryId(),
                answerSummary(),
                answerExplanation(),
                evidence,
                regionIds,
                sourceIds,
                List.of(),
                VisualizationType.MAP,
                ConfidenceLevel.HIGH
        );

        evidence.clear();
        regionIds.clear();
        sourceIds.clear();

        assertThat(answer.getEvidence()).hasSize(1);
        assertThat(answer.getRegionIds()).hasSize(1);
        assertThat(answer.getSourceIds()).hasSize(1);
        assertThat(answer.getWarnings()).isEmpty();
        assertThatThrownBy(() -> answer.getEvidence().add(answerEvidence()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldAllowLowConfidenceAnswerWithoutEvidenceOrSourcesWhenWarningExists() {
        AiAnswer answer = new AiAnswer(
                aiAnswerId(),
                aiQueryId(),
                answerSummary(),
                answerExplanation(),
                null,
                null,
                null,
                List.of(answerWarning()),
                VisualizationType.NONE,
                ConfidenceLevel.LOW
        );

        assertThat(answer.getEvidence()).isEmpty();
        assertThat(answer.getRegionIds()).isEmpty();
        assertThat(answer.getSourceIds()).isEmpty();
        assertThat(answer.getWarnings()).hasSize(1);
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> validAnswerWith(null, aiQueryId(), answerSummary(), answerExplanation(), VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("AI answer id cannot be null");

        assertThatThrownBy(() -> validAnswerWith(aiAnswerId(), null, answerSummary(), answerExplanation(), VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("AI query id cannot be null");

        assertThatThrownBy(() -> validAnswerWith(aiAnswerId(), aiQueryId(), null, answerExplanation(), VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Answer summary cannot be null");

        assertThatThrownBy(() -> validAnswerWith(aiAnswerId(), aiQueryId(), answerSummary(), null, VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Answer explanation cannot be null");

        assertThatThrownBy(() -> validAnswerWith(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), null, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Suggested visualization cannot be null");

        assertThatThrownBy(() -> validAnswerWith(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), VisualizationType.MAP, null))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Confidence level cannot be null");
    }

    @Test
    void shouldRejectNullElementsInsideLists() {
        assertThatThrownBy(() -> new AiAnswer(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), Collections.singletonList((AnswerEvidence) null), List.of(regionId()), List.of(dataSourceId()), List.of(), VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Evidence cannot contain null values");

        assertThatThrownBy(() -> new AiAnswer(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), List.of(answerEvidence()), Collections.singletonList((RegionId) null), List.of(dataSourceId()), List.of(), VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Region ids cannot contain null values");

        assertThatThrownBy(() -> new AiAnswer(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), List.of(answerEvidence()), List.of(regionId()), Collections.singletonList((DataSourceId) null), List.of(), VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Source ids cannot contain null values");

        assertThatThrownBy(() -> new AiAnswer(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), List.of(answerEvidence()), List.of(regionId()), List.of(dataSourceId()), Collections.singletonList((AnswerWarning) null), VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Warnings cannot contain null values");
    }

    @Test
    void shouldEnforceEvidenceSourceAndConfidenceConsistency() {
        assertThatThrownBy(() -> new AiAnswer(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), List.of(), List.of(regionId()), List.of(dataSourceId()), List.of(answerWarning()), VisualizationType.MAP, ConfidenceLevel.HIGH))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Answer without evidence or sources must have low confidence");

        assertThatThrownBy(() -> new AiAnswer(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), List.of(answerEvidence()), List.of(regionId()), List.of(), List.of(answerWarning()), VisualizationType.MAP, ConfidenceLevel.MEDIUM))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Answer without evidence or sources must have low confidence");

        assertThatThrownBy(() -> new AiAnswer(aiAnswerId(), aiQueryId(), answerSummary(), answerExplanation(), List.of(answerEvidence()), List.of(regionId()), List.of(dataSourceId()), List.of(), VisualizationType.MAP, ConfidenceLevel.LOW))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Low confidence answer must contain at least one warning");
    }

    @Test
    void shouldCompareAnswersByIdentity() {
        AiAnswerId sameId = aiAnswerId();
        AiAnswer first = new AiAnswer(sameId, aiQueryId(), answerSummary(), answerExplanation(), List.of(answerEvidence()), List.of(regionId()), List.of(dataSourceId()), List.of(), VisualizationType.MAP, ConfidenceLevel.HIGH);
        AiAnswer second = new AiAnswer(sameId, aiQueryId(), new AnswerSummary("Resumen distinto valido"), answerExplanation(), List.of(answerEvidence()), List.of(), List.of(dataSourceId()), List.of(), VisualizationType.TABLE, ConfidenceLevel.MEDIUM);

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(aiAnswer());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not an answer");
    }

    private AiAnswer validAnswerWith(
            AiAnswerId id,
            com.appbit.geoanalytics.domain.ai.vo.AiQueryId queryId,
            AnswerSummary summary,
            AnswerExplanation explanation,
            VisualizationType visualizationType,
            ConfidenceLevel confidenceLevel
    ) {
        return new AiAnswer(
                id,
                queryId,
                summary,
                explanation,
                List.of(answerEvidence()),
                List.of(regionId()),
                List.of(dataSourceId()),
                List.of(),
                visualizationType,
                confidenceLevel
        );
    }

    @Nested
    class AiAnswerIdTest {

        @Test
        void shouldAcceptUuidV7() {
            AiAnswerId id = new AiAnswerId(uuidV7());

            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new AiAnswerId(null))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("AI answer id cannot be null");

            assertThatThrownBy(() -> new AiAnswerId(nilUuid()))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("AI answer id cannot be nil UUID");

            assertThatThrownBy(() -> new AiAnswerId(uuidV4()))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("AI answer id must be UUIDv7");

            assertThatThrownBy(() -> new AiAnswerId(nonRfc4122UuidV7()))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("AI answer id must be RFC 4122 compatible");
        }
    }

    @Nested
    class AnswerTextValueObjectsTest {

        @Test
        void shouldTrimValidTexts() {
            assertThat(new AnswerSummary("  resumen valido  ").value()).isEqualTo("resumen valido");
            assertThat(new AnswerExplanation("  explicacion valida  ").value()).isEqualTo("explicacion valida");
            assertThat(new AnswerEvidence("  evidencia valida  ").value()).isEqualTo("evidencia valida");
            assertThat(new AnswerWarning("  alerta valida  ").value()).isEqualTo("alerta valida");
        }

        @Test
        void shouldRejectInvalidSummary() {
            assertThatThrownBy(() -> new AnswerSummary(null)).isInstanceOf(AiDomainException.class).hasMessage("Answer summary cannot be null");
            assertThatThrownBy(() -> new AnswerSummary("   ")).isInstanceOf(AiDomainException.class).hasMessage("Answer summary cannot be blank");
            assertThatThrownBy(() -> new AnswerSummary("abcd")).isInstanceOf(AiDomainException.class).hasMessage("Answer summary length must be between 5 and 1000 characters");
            assertThatThrownBy(() -> new AnswerSummary(text(1001))).isInstanceOf(AiDomainException.class).hasMessage("Answer summary length must be between 5 and 1000 characters");
            assertThatThrownBy(() -> new AnswerSummary("val\nido")).isInstanceOf(AiDomainException.class).hasMessage("Answer summary cannot contain control characters");
        }

        @Test
        void shouldRejectInvalidExplanation() {
            assertThatThrownBy(() -> new AnswerExplanation(null)).isInstanceOf(AiDomainException.class).hasMessage("Answer explanation cannot be null");
            assertThatThrownBy(() -> new AnswerExplanation("   ")).isInstanceOf(AiDomainException.class).hasMessage("Answer explanation cannot be blank");
            assertThatThrownBy(() -> new AnswerExplanation("123456789")).isInstanceOf(AiDomainException.class).hasMessage("Answer explanation length must be between 10 and 4000 characters");
            assertThatThrownBy(() -> new AnswerExplanation(text(4001))).isInstanceOf(AiDomainException.class).hasMessage("Answer explanation length must be between 10 and 4000 characters");
            assertThatThrownBy(() -> new AnswerExplanation("explicacion\nvalida")).isInstanceOf(AiDomainException.class).hasMessage("Answer explanation cannot contain control characters");
        }

        @Test
        void shouldRejectInvalidEvidence() {
            assertThatThrownBy(() -> new AnswerEvidence(null)).isInstanceOf(AiDomainException.class).hasMessage("Answer evidence cannot be null");
            assertThatThrownBy(() -> new AnswerEvidence("   ")).isInstanceOf(AiDomainException.class).hasMessage("Answer evidence cannot be blank");
            assertThatThrownBy(() -> new AnswerEvidence("abcd")).isInstanceOf(AiDomainException.class).hasMessage("Answer evidence length must be between 5 and 1000 characters");
            assertThatThrownBy(() -> new AnswerEvidence(text(1001))).isInstanceOf(AiDomainException.class).hasMessage("Answer evidence length must be between 5 and 1000 characters");
            assertThatThrownBy(() -> new AnswerEvidence("val\nido")).isInstanceOf(AiDomainException.class).hasMessage("Answer evidence cannot contain control characters");
        }

        @Test
        void shouldRejectInvalidWarning() {
            assertThatThrownBy(() -> new AnswerWarning(null)).isInstanceOf(AiDomainException.class).hasMessage("Answer warning cannot be null");
            assertThatThrownBy(() -> new AnswerWarning("   ")).isInstanceOf(AiDomainException.class).hasMessage("Answer warning cannot be blank");
            assertThatThrownBy(() -> new AnswerWarning("abcd")).isInstanceOf(AiDomainException.class).hasMessage("Answer warning length must be between 5 and 500 characters");
            assertThatThrownBy(() -> new AnswerWarning(text(501))).isInstanceOf(AiDomainException.class).hasMessage("Answer warning length must be between 5 and 500 characters");
            assertThatThrownBy(() -> new AnswerWarning("val\nido")).isInstanceOf(AiDomainException.class).hasMessage("Answer warning cannot contain control characters");
        }
    }
}
