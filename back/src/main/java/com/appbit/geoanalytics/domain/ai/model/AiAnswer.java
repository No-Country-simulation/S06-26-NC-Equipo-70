package com.appbit.geoanalytics.domain.ai.model;

import com.appbit.geoanalytics.domain.ai.enums.VisualizationType;
import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;
import com.appbit.geoanalytics.domain.ai.vo.*;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.enums.ConfidenceLevel;
import com.appbit.geoanalytics.domain.source.vo.DataSourceId;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Getter
public final class AiAnswer {

    private final AiAnswerId id;
    private final AiQueryId queryId;
    private final AnswerSummary summary;
    private final AnswerExplanation explanation;
    private final List<AnswerEvidence> evidence;
    private final List<RegionId> regionIds;
    private final List<DataSourceId> sourceIds;
    private final List<AnswerWarning> warnings;
    private final VisualizationType suggestedVisualization;
    private final ConfidenceLevel confidenceLevel;

    @Builder
    public AiAnswer(
            AiAnswerId id,
            AiQueryId queryId,
            AnswerSummary summary,
            AnswerExplanation explanation,
            @Nullable List<AnswerEvidence> evidence,
            @Nullable List<RegionId> regionIds,
            @Nullable List<DataSourceId> sourceIds,
            @Nullable List<AnswerWarning> warnings,
            VisualizationType suggestedVisualization,
            ConfidenceLevel confidenceLevel
    ) {
        if (id == null) {
            throw new AiDomainException("AI answer id cannot be null");
        }

        if (queryId == null) {
            throw new AiDomainException("AI query id cannot be null");
        }

        if (summary == null) {
            throw new AiDomainException("Answer summary cannot be null");
        }

        if (explanation == null) {
            throw new AiDomainException("Answer explanation cannot be null");
        }

        if (suggestedVisualization == null) {
            throw new AiDomainException("Suggested visualization cannot be null");
        }

        if (confidenceLevel == null) {
            throw new AiDomainException("Confidence level cannot be null");
        }

        List<AnswerEvidence> normalizedEvidence = normalizeList(evidence, "Evidence");
        List<RegionId> normalizedRegionIds = normalizeList(regionIds, "Region ids");
        List<DataSourceId> normalizedSourceIds = normalizeList(sourceIds, "Source ids");
        List<AnswerWarning> normalizedWarnings = normalizeList(warnings, "Warnings");

        validateEvidenceConsistency(
                normalizedEvidence,
                normalizedSourceIds,
                normalizedWarnings,
                confidenceLevel
        );

        this.id = id;
        this.queryId = queryId;
        this.summary = summary;
        this.explanation = explanation;
        this.evidence = normalizedEvidence;
        this.regionIds = normalizedRegionIds;
        this.sourceIds = normalizedSourceIds;
        this.warnings = normalizedWarnings;
        this.suggestedVisualization = suggestedVisualization;
        this.confidenceLevel = confidenceLevel;
    }

    private <T> List<T> normalizeList(@Nullable List<T> values, String fieldName) {
        if (values == null) {
            return List.of();
        }

        for (T value : values) {
            if (value == null) {
                throw new AiDomainException(fieldName + " cannot contain null values");
            }
        }

        return List.copyOf(values);
    }

    private void validateEvidenceConsistency(
            List<AnswerEvidence> evidence,
            List<DataSourceId> sourceIds,
            List<AnswerWarning> warnings,
            ConfidenceLevel confidenceLevel
    ) {
        if ((evidence.isEmpty() || sourceIds.isEmpty()) && confidenceLevel != ConfidenceLevel.LOW) {
            throw new AiDomainException("Answer without evidence or sources must have low confidence");
        }

        if (confidenceLevel == ConfidenceLevel.LOW && warnings.isEmpty()) {
            throw new AiDomainException("Low confidence answer must contain at least one warning");
        }

        if (evidence.isEmpty() && warnings.isEmpty()) {
            throw new AiDomainException("Answer without evidence must contain at least one warning");
        }

        if (sourceIds.isEmpty() && warnings.isEmpty()) {
            throw new AiDomainException("Answer without sources must contain at least one warning");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AiAnswer that = (AiAnswer) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}