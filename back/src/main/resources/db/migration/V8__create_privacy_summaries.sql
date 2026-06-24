CREATE TABLE privacy_summaries (
    id UUID PRIMARY KEY,

    source_id UUID NOT NULL,
    parameter VARCHAR(80) NOT NULL,
    value VARCHAR(200) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_privacy_summaries_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT uk_privacy_summaries_source_parameter
        UNIQUE (source_id, parameter),

    CONSTRAINT ck_privacy_summaries_parameter_not_blank
        CHECK (length(btrim(parameter)) > 0),

    CONSTRAINT ck_privacy_summaries_value_not_blank
        CHECK (length(btrim(value)) > 0)
);
