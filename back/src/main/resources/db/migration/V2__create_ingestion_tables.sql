CREATE TABLE ingestion_runs (
    id UUID PRIMARY KEY,

    source_id UUID NOT NULL,
    file_name VARCHAR(120) NOT NULL,
    status VARCHAR(16) NOT NULL,

    rows_read BIGINT NOT NULL DEFAULT 0,
    rows_inserted BIGINT NOT NULL DEFAULT 0,
    rows_rejected BIGINT NOT NULL DEFAULT 0,

    started_at TIMESTAMPTZ NOT NULL,
    finished_at TIMESTAMPTZ,
    error_message VARCHAR(1000),

    CONSTRAINT fk_ingestion_runs_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT ck_ingestion_runs_file_name_length
        CHECK (char_length(file_name) BETWEEN 5 AND 120),

    CONSTRAINT ck_ingestion_runs_file_name_not_blank
        CHECK (length(btrim(file_name)) > 0),

    CONSTRAINT ck_ingestion_runs_file_name_csv_extension
        CHECK (lower(file_name) LIKE '%.csv'),

    CONSTRAINT ck_ingestion_runs_file_name_no_path
        CHECK (
            position('/' IN file_name) = 0
            AND position(chr(92) IN file_name) = 0
            AND position('..' IN file_name) = 0
        ),

    CONSTRAINT ck_ingestion_runs_status
        CHECK (
            status IN (
                'PENDING',
                'RUNNING',
                'COMPLETED',
                'FAILED',
                'SKIPPED'
            )
        ),

    CONSTRAINT ck_ingestion_runs_rows_read_non_negative
        CHECK (rows_read >= 0),

    CONSTRAINT ck_ingestion_runs_rows_inserted_non_negative
        CHECK (rows_inserted >= 0),

    CONSTRAINT ck_ingestion_runs_rows_rejected_non_negative
        CHECK (rows_rejected >= 0),

    CONSTRAINT ck_ingestion_runs_rows_total_consistency
        CHECK ((rows_inserted + rows_rejected) <= rows_read),

    CONSTRAINT ck_ingestion_runs_finished_at_state_consistency
        CHECK (
            (
                status IN ('PENDING', 'RUNNING')
                AND finished_at IS NULL
            )
            OR (
                status IN ('COMPLETED', 'FAILED', 'SKIPPED')
                AND finished_at IS NOT NULL
            )
        ),

    CONSTRAINT ck_ingestion_runs_finished_at_after_started_at
        CHECK (
            finished_at IS NULL
            OR finished_at >= started_at
        ),

    CONSTRAINT ck_ingestion_runs_error_required_when_failed_or_skipped
        CHECK (
            status NOT IN ('FAILED', 'SKIPPED')
            OR (
                error_message IS NOT NULL
                AND length(btrim(error_message)) > 0
            )
        )
);
