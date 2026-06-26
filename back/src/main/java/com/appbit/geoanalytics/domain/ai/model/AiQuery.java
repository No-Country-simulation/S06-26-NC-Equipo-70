package com.appbit.geoanalytics.domain.ai.model;

import com.appbit.geoanalytics.domain.ai.enums.AiIntent;
import com.appbit.geoanalytics.domain.ai.enums.Language;
import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;
import com.appbit.geoanalytics.domain.ai.vo.AiQueryId;
import com.appbit.geoanalytics.domain.ai.vo.QueryText;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.vo.Period;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.List;

@Getter
public final class AiQuery {

    private final AiQueryId id;
    private final QueryText queryText;
    private final Language language;
    private final AiIntent detectedIntent;
    private final List<RegionId> regionIds;

    @Nullable
    private final Period period;

    @Builder
    public AiQuery(
            AiQueryId id,
            QueryText queryText,
            Language language,
            AiIntent detectedIntent,
            @Nullable List<RegionId> regionIds,
            @Nullable Period period
    ) {
        if (id == null) {
            throw new AiDomainException("AI query id cannot be null");
        }

        if (queryText == null) {
            throw new AiDomainException("Query text cannot be null");
        }

        if (language == null) {
            throw new AiDomainException("Language cannot be null");
        }

        if (detectedIntent == null) {
            throw new AiDomainException("Detected intent cannot be null");
        }

        this.id = id;
        this.queryText = queryText;
        this.language = language;
        this.detectedIntent = detectedIntent;
        this.regionIds = normalizeList(regionIds, "Region ids");
        this.period = period;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AiQuery that = (AiQuery) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}