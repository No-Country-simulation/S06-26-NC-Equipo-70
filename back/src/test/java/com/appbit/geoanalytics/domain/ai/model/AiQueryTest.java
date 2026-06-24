package com.appbit.geoanalytics.domain.ai.model;

import com.appbit.geoanalytics.domain.ai.enums.AiIntent;
import com.appbit.geoanalytics.domain.ai.enums.Language;
import com.appbit.geoanalytics.domain.ai.exception.AiDomainException;
import com.appbit.geoanalytics.domain.ai.vo.AiQueryId;
import com.appbit.geoanalytics.domain.ai.vo.QueryText;
import com.appbit.geoanalytics.domain.region.vo.RegionId;
import com.appbit.geoanalytics.domain.shared.enums.PeriodType;
import com.appbit.geoanalytics.domain.shared.vo.Period;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.appbit.geoanalytics.domain.testing.DomainFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AiQueryTest {

    @Test
    void shouldCreateValidQueryAndNormalizeRegionIds() {
        RegionId regionId = regionId();
        List<RegionId> mutableRegionIds = new ArrayList<>(List.of(regionId));

        AiQuery query = new AiQuery(
                aiQueryId(),
                new QueryText("  conectividad por region  "),
                Language.ES,
                AiIntent.CONNECTIVITY_GAP,
                mutableRegionIds,
                new Period(PeriodType.TARDE)
        );

        mutableRegionIds.clear();

        assertThat(query.getQueryText().value()).isEqualTo("conectividad por region");
        assertThat(query.getRegionIds()).containsExactly(regionId);
        assertThat(query.getPeriod()).isEqualTo(new Period(PeriodType.TARDE));
        assertThatThrownBy(() -> query.getRegionIds().add(regionId()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldUseEmptyRegionListWhenRegionIdsAreNull() {
        AiQuery query = new AiQuery(
                aiQueryId(),
                queryText(),
                Language.ES,
                AiIntent.CONNECTIVITY_GAP,
                null,
                null
        );

        assertThat(query.getRegionIds()).isEmpty();
        assertThat(query.getPeriod()).isNull();
    }

    @Test
    void shouldRejectNullRequiredFields() {
        assertThatThrownBy(() -> new AiQuery(null, queryText(), Language.ES, AiIntent.CONNECTIVITY_GAP, List.of(regionId()), period()))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("AI query id cannot be null");

        assertThatThrownBy(() -> new AiQuery(aiQueryId(), null, Language.ES, AiIntent.CONNECTIVITY_GAP, List.of(regionId()), period()))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Query text cannot be null");

        assertThatThrownBy(() -> new AiQuery(aiQueryId(), queryText(), null, AiIntent.CONNECTIVITY_GAP, List.of(regionId()), period()))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Language cannot be null");

        assertThatThrownBy(() -> new AiQuery(aiQueryId(), queryText(), Language.ES, null, List.of(regionId()), period()))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Detected intent cannot be null");
    }

    @Test
    void shouldRejectNullRegionInsideRegionList() {
        assertThatThrownBy(() -> new AiQuery(aiQueryId(), queryText(), Language.ES, AiIntent.CONNECTIVITY_GAP, java.util.Collections.singletonList((RegionId) null), period()))
                .isInstanceOf(AiDomainException.class)
                .hasMessage("Region ids cannot contain null values");
    }

    @Test
    void shouldCompareQueriesByIdentity() {
        AiQueryId sameId = aiQueryId();
        AiQuery first = new AiQuery(sameId, queryText(), Language.ES, AiIntent.CONNECTIVITY_GAP, List.of(regionId()), period());
        AiQuery second = new AiQuery(sameId, new QueryText("otra consulta valida"), Language.EN, AiIntent.UNKNOWN, List.of(), null);

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(aiQuery());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not a query");
    }

    @Nested
    class QueryTextTest {

        @Test
        void shouldTrimText() {
            QueryText text = new QueryText("  abc  ");

            assertThat(text.value()).isEqualTo("abc");
        }

        @Test
        void shouldRejectInvalidText() {
            assertThatThrownBy(() -> new QueryText(null))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("Query text cannot be null");

            assertThatThrownBy(() -> new QueryText("   "))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("Query text cannot be blank");

            assertThatThrownBy(() -> new QueryText("ab"))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("Query text length must be between 3 and 500 characters");

            assertThatThrownBy(() -> new QueryText(text(501)))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("Query text length must be between 3 and 500 characters");

            assertThatThrownBy(() -> new QueryText("ab\nc"))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("Query text cannot contain control characters");
        }
    }

    @Nested
    class AiQueryIdTest {

        @Test
        void shouldAcceptUuidV7() {
            AiQueryId id = new AiQueryId(uuidV7());

            assertThat(id.value().version()).isEqualTo(7);
            assertThat(id.value().variant()).isEqualTo(2);
        }

        @Test
        void shouldRejectInvalidUuid() {
            assertThatThrownBy(() -> new AiQueryId(null))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("AI query id cannot be null");

            assertThatThrownBy(() -> new AiQueryId(nilUuid()))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("AI query id cannot be nil UUID");

            assertThatThrownBy(() -> new AiQueryId(uuidV4()))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("AI query id must be UUIDv7");

            assertThatThrownBy(() -> new AiQueryId(nonRfc4122UuidV7()))
                    .isInstanceOf(AiDomainException.class)
                    .hasMessage("AI query id must be RFC 4122 compatible");
        }
    }
}
