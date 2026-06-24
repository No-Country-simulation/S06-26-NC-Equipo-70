CREATE TABLE network_indicators (
    id UUID PRIMARY KEY,

    region_id UUID NOT NULL,
    source_id UUID NOT NULL,

    indicator_type VARCHAR(40) NOT NULL,
    score NUMERIC(7, 6) NOT NULL,
    unit VARCHAR(32) NOT NULL,

    gap_level VARCHAR(16) NOT NULL,
    confidence_level VARCHAR(16) NOT NULL,
    period VARCHAR(16) NOT NULL,
    description VARCHAR(500) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_network_indicators_region
        FOREIGN KEY (region_id)
        REFERENCES regions (id),

    CONSTRAINT fk_network_indicators_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT uk_network_indicators_region_source_type_period
        UNIQUE (region_id, source_id, indicator_type, period),

    CONSTRAINT ck_network_indicators_type
        CHECK (
            indicator_type IN (
                'CONNECTIVITY_INDEX',
                'COVERAGE_INDEX',
                'QUALITY_INDEX',
                'CONGESTION_INDEX',
                'DROP_RATE_INDEX'
            )
        ),

    CONSTRAINT ck_network_indicators_score_range
        CHECK (score BETWEEN 0 AND 1),

    CONSTRAINT ck_network_indicators_unit
        CHECK (
            unit IN (
                'INDEX',
                'PERCENTAGE',
                'SCORE',
                'ACTIVE_USERS',
                'PROGRAMS',
                'SERVICES',
                'BYTES',
                'SECONDS'
            )
        ),

    CONSTRAINT ck_network_indicators_gap_level
        CHECK (
            gap_level IN (
                'LOW',
                'MEDIUM',
                'HIGH',
                'CRITICAL',
                'UNKNOWN'
            )
        ),

    CONSTRAINT ck_network_indicators_confidence_level
        CHECK (confidence_level IN ('LOW', 'MEDIUM', 'HIGH')),

    CONSTRAINT ck_network_indicators_period
        CHECK (period IN ('MADRUGADA', 'MANHA', 'TARDE', 'NOITE')),

    CONSTRAINT ck_network_indicators_description_not_blank
        CHECK (length(btrim(description)) > 0)
);

CREATE TABLE social_indicators (
    id UUID PRIMARY KEY,

    region_id UUID NOT NULL,
    source_id UUID NOT NULL,

    indicator_type VARCHAR(40) NOT NULL,
    score NUMERIC(7, 6) NOT NULL,
    unit VARCHAR(32) NOT NULL,

    gap_level VARCHAR(16) NOT NULL,
    confidence_level VARCHAR(16) NOT NULL,
    description VARCHAR(500) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_social_indicators_region
        FOREIGN KEY (region_id)
        REFERENCES regions (id),

    CONSTRAINT fk_social_indicators_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT uk_social_indicators_region_source_type
        UNIQUE (region_id, source_id, indicator_type),

    CONSTRAINT ck_social_indicators_type
        CHECK (
            indicator_type IN (
                'TRAINING',
                'EMPLOYABILITY',
                'SOCIAL_EXPERIENCE',
                'MENTORSHIP',
                'MENTAL_HEALTH'
            )
        ),

    CONSTRAINT ck_social_indicators_score_range
        CHECK (score BETWEEN 0 AND 1),

    CONSTRAINT ck_social_indicators_unit
        CHECK (
            unit IN (
                'INDEX',
                'PERCENTAGE',
                'SCORE',
                'ACTIVE_USERS',
                'PROGRAMS',
                'SERVICES',
                'BYTES',
                'SECONDS'
            )
        ),

    CONSTRAINT ck_social_indicators_gap_level
        CHECK (
            gap_level IN (
                'LOW',
                'MEDIUM',
                'HIGH',
                'CRITICAL',
                'UNKNOWN'
            )
        ),

    CONSTRAINT ck_social_indicators_confidence_level
        CHECK (confidence_level IN ('LOW', 'MEDIUM', 'HIGH')),

    CONSTRAINT ck_social_indicators_description_not_blank
        CHECK (length(btrim(description)) > 0)
);
