CREATE TABLE travel_distance_metrics (
    id UUID PRIMARY KEY,

    source_id UUID NOT NULL,

    origin_region_id UUID NOT NULL,
    destination_region_id UUID NOT NULL,

    origin_cluster_name VARCHAR(40) NOT NULL,
    destination_cluster_name VARCHAR(40) NOT NULL,

    same_cluster BOOLEAN NOT NULL,
    observations BIGINT NOT NULL DEFAULT 0,

    average_distance_km NUMERIC(10, 3) NOT NULL,
    p25_distance_km NUMERIC(10, 3) NOT NULL,
    p75_distance_km NUMERIC(10, 3) NOT NULL,

    predominant_period VARCHAR(16) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_travel_distance_metrics_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT fk_travel_distance_metrics_origin_region
        FOREIGN KEY (origin_region_id)
        REFERENCES regions (id),

    CONSTRAINT fk_travel_distance_metrics_destination_region
        FOREIGN KEY (destination_region_id)
        REFERENCES regions (id),

    CONSTRAINT uk_travel_distance_metrics_source_origin_destination_period
        UNIQUE (source_id, origin_region_id, destination_region_id, predominant_period),

    CONSTRAINT ck_travel_distance_metrics_origin_cluster_name_not_blank
        CHECK (length(btrim(origin_cluster_name)) > 0),

    CONSTRAINT ck_travel_distance_metrics_destination_cluster_name_not_blank
        CHECK (length(btrim(destination_cluster_name)) > 0),

    CONSTRAINT ck_travel_distance_metrics_observations_non_negative
        CHECK (observations >= 0),

    CONSTRAINT ck_travel_distance_metrics_average_distance_non_negative
        CHECK (average_distance_km >= 0),

    CONSTRAINT ck_travel_distance_metrics_p25_distance_non_negative
        CHECK (p25_distance_km >= 0),

    CONSTRAINT ck_travel_distance_metrics_p75_distance_non_negative
        CHECK (p75_distance_km >= 0),

    CONSTRAINT ck_travel_distance_metrics_percentile_consistency
        CHECK (
            p25_distance_km <= average_distance_km
            AND average_distance_km <= p75_distance_km
        ),

    CONSTRAINT ck_travel_distance_metrics_predominant_period
        CHECK (predominant_period IN ('MADRUGADA', 'MANHA', 'TARDE', 'NOITE'))
);
