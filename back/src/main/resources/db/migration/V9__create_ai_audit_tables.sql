CREATE TABLE ai_queries (
    id UUID PRIMARY KEY,

    request_id UUID NOT NULL,
    query_text VARCHAR(500) NOT NULL,
    language VARCHAR(2) NOT NULL DEFAULT 'ES',
    intent VARCHAR(40) NOT NULL,
    filters JSONB NOT NULL DEFAULT '{}'::jsonb,
    status VARCHAR(32) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_ai_queries_request_id
        UNIQUE (request_id),

    CONSTRAINT ck_ai_queries_text_not_blank
        CHECK (length(btrim(query_text)) > 0),

    CONSTRAINT ck_ai_queries_language
        CHECK (language IN ('ES', 'EN', 'PT')),

    CONSTRAINT ck_ai_queries_intent
        CHECK (
            intent IN (
                'TRAINING_GAP',
                'EMPLOYABILITY_GAP',
                'MENTAL_HEALTH_ACCESS',
                'MENTORSHIP_NEED',
                'SOCIAL_EXPERIENCE',
                'CONNECTIVITY_GAP',
                'POPULATION_CONCENTRATION',
                'REGION_COMPARISON',
                'SOURCE_EXPLANATION',
                'UNKNOWN'
            )
        ),

    CONSTRAINT ck_ai_queries_status
        CHECK (
            status IN (
                'PROCESSED',
                'INSUFFICIENT_EVIDENCE',
                'FAILED'
            )
        )
);

CREATE TABLE ai_answers (
    id UUID PRIMARY KEY,

    query_id UUID NOT NULL,

    summary VARCHAR(1000) NOT NULL,
    explanation VARCHAR(4000) NOT NULL,
    data JSONB NOT NULL DEFAULT '{}'::jsonb,

    suggested_visualization VARCHAR(16) NOT NULL,
    confidence_level VARCHAR(16) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ai_answers_query
        FOREIGN KEY (query_id)
        REFERENCES ai_queries (id)
        ON DELETE CASCADE,

    CONSTRAINT uk_ai_answers_query
        UNIQUE (query_id),

    CONSTRAINT ck_ai_answers_summary_not_blank
        CHECK (length(btrim(summary)) > 0),

    CONSTRAINT ck_ai_answers_explanation_not_blank
        CHECK (length(btrim(explanation)) > 0),

    CONSTRAINT ck_ai_answers_suggested_visualization
        CHECK (
            suggested_visualization IN (
                'MAP',
                'TABLE',
                'RANKING',
                'FLOW',
                'NONE'
            )
        ),

    CONSTRAINT ck_ai_answers_confidence_level
        CHECK (confidence_level IN ('LOW', 'MEDIUM', 'HIGH'))
);

CREATE TABLE ai_answer_evidence (
    answer_id UUID NOT NULL,
    position INTEGER NOT NULL,
    evidence_text VARCHAR(1000) NOT NULL,

    PRIMARY KEY (answer_id, position),

    CONSTRAINT fk_ai_answer_evidence_answer
        FOREIGN KEY (answer_id)
        REFERENCES ai_answers (id)
        ON DELETE CASCADE,

    CONSTRAINT ck_ai_answer_evidence_position_non_negative
        CHECK (position >= 0),

    CONSTRAINT ck_ai_answer_evidence_text_not_blank
        CHECK (length(btrim(evidence_text)) >= 5)
);

CREATE TABLE ai_answer_regions (
    answer_id UUID NOT NULL,
    region_id UUID NOT NULL,
    position INTEGER NOT NULL DEFAULT 0,

    PRIMARY KEY (answer_id, region_id),

    CONSTRAINT fk_ai_answer_regions_answer
        FOREIGN KEY (answer_id)
        REFERENCES ai_answers (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ai_answer_regions_region
        FOREIGN KEY (region_id)
        REFERENCES regions (id),

    CONSTRAINT ck_ai_answer_regions_position_non_negative
        CHECK (position >= 0)
);

CREATE TABLE ai_answer_sources (
    answer_id UUID NOT NULL,
    source_id UUID NOT NULL,
    position INTEGER NOT NULL DEFAULT 0,

    PRIMARY KEY (answer_id, source_id),

    CONSTRAINT fk_ai_answer_sources_answer
        FOREIGN KEY (answer_id)
        REFERENCES ai_answers (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ai_answer_sources_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT ck_ai_answer_sources_position_non_negative
        CHECK (position >= 0)
);

CREATE TABLE ai_answer_warnings (
    answer_id UUID NOT NULL,
    position INTEGER NOT NULL,
    warning_text VARCHAR(500) NOT NULL,

    PRIMARY KEY (answer_id, position),

    CONSTRAINT fk_ai_answer_warnings_answer
        FOREIGN KEY (answer_id)
        REFERENCES ai_answers (id)
        ON DELETE CASCADE,

    CONSTRAINT ck_ai_answer_warnings_position_non_negative
        CHECK (position >= 0),

    CONSTRAINT ck_ai_answer_warnings_text_not_blank
        CHECK (length(btrim(warning_text)) >= 5)
);
