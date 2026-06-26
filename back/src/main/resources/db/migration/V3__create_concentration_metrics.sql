CREATE TABLE concentration_metrics (
    id UUID PRIMARY KEY,

    source_id UUID NOT NULL,
    region_id UUID NOT NULL,
    ecgi VARCHAR(16) NOT NULL,

    cluster_name VARCHAR(40) NOT NULL,
    municipality VARCHAR(60) NOT NULL,

    day_date DATE NOT NULL,
    period VARCHAR(16) NOT NULL,

    active_users BIGINT NOT NULL DEFAULT 0,
    sessions BIGINT NOT NULL DEFAULT 0,
    download_bytes BIGINT NOT NULL DEFAULT 0,
    upload_bytes BIGINT NOT NULL DEFAULT 0,

    average_session_duration_seconds INTEGER NOT NULL DEFAULT 0,
    average_drop_rate NUMERIC(7, 6) NOT NULL DEFAULT 0,
    average_congestion NUMERIC(7, 6) NOT NULL DEFAULT 0,

    total_calls INTEGER NOT NULL DEFAULT 0,
    total_messages INTEGER NOT NULL DEFAULT 0,

    latitude NUMERIC(9, 6) NOT NULL,
    longitude NUMERIC(9, 6) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_concentration_metrics_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT fk_concentration_metrics_region
        FOREIGN KEY (region_id)
        REFERENCES regions (id),

    CONSTRAINT fk_concentration_metrics_ecgi
        FOREIGN KEY (ecgi)
        REFERENCES antennas (ecgi),

    CONSTRAINT ck_concentration_metrics_ecgi_length
        CHECK (char_length(ecgi) BETWEEN 12 AND 16),

    CONSTRAINT ck_concentration_metrics_ecgi_digits_only
        CHECK (ecgi ~ '^[0-9]+$'),

    CONSTRAINT ck_concentration_metrics_cluster_name_not_blank
        CHECK (length(btrim(cluster_name)) > 0),

    CONSTRAINT ck_concentration_metrics_municipality_not_blank
        CHECK (length(btrim(municipality)) > 0),

    CONSTRAINT ck_concentration_metrics_period
        CHECK (period IN ('MADRUGADA', 'MANHA', 'TARDE', 'NOITE')),

    CONSTRAINT ck_concentration_metrics_active_users_non_negative
        CHECK (active_users >= 0),

    CONSTRAINT ck_concentration_metrics_sessions_non_negative
        CHECK (sessions >= 0),

    CONSTRAINT ck_concentration_metrics_download_bytes_non_negative
        CHECK (download_bytes >= 0),

    CONSTRAINT ck_concentration_metrics_upload_bytes_non_negative
        CHECK (upload_bytes >= 0),

    CONSTRAINT ck_concentration_metrics_average_session_duration_non_negative
        CHECK (average_session_duration_seconds >= 0),

    CONSTRAINT ck_concentration_metrics_average_drop_rate_range
        CHECK (average_drop_rate BETWEEN 0 AND 1),

    CONSTRAINT ck_concentration_metrics_average_congestion_range
        CHECK (average_congestion BETWEEN 0 AND 1),

    CONSTRAINT ck_concentration_metrics_total_calls_non_negative
        CHECK (total_calls >= 0),

    CONSTRAINT ck_concentration_metrics_total_messages_non_negative
        CHECK (total_messages >= 0),

    CONSTRAINT ck_concentration_metrics_latitude_range
        CHECK (latitude BETWEEN -90 AND 90),

    CONSTRAINT ck_concentration_metrics_longitude_range
        CHECK (longitude BETWEEN -180 AND 180),

    CONSTRAINT ck_concentration_metrics_coordinates_not_zero_zero
        CHECK (NOT (latitude = 0 AND longitude = 0)),

    CONSTRAINT uk_concentration_metrics_source_ecgi_day_period
        UNIQUE (source_id, ecgi, day_date, period)
);
