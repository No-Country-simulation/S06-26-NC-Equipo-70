CREATE TABLE origin_destination_flows (
    id UUID PRIMARY KEY,

    source_id UUID NOT NULL,

    origin_region_id UUID NOT NULL,
    destination_region_id UUID NOT NULL,

    origin_cluster_name VARCHAR(40) NOT NULL,
    destination_cluster_name VARCHAR(40) NOT NULL,
    origin_municipality VARCHAR(60) NOT NULL,
    destination_municipality VARCHAR(60) NOT NULL,

    origin_latitude NUMERIC(9, 6) NOT NULL,
    origin_longitude NUMERIC(9, 6) NOT NULL,
    destination_latitude NUMERIC(9, 6) NOT NULL,
    destination_longitude NUMERIC(9, 6) NOT NULL,

    same_cluster BOOLEAN NOT NULL,
    users_count BIGINT NOT NULL DEFAULT 0,
    trips_count BIGINT NOT NULL DEFAULT 0,
    average_distance_km NUMERIC(10, 3) NOT NULL,
    predominant_period VARCHAR(16) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_origin_destination_flows_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT fk_origin_destination_flows_origin_region
        FOREIGN KEY (origin_region_id)
        REFERENCES regions (id),

    CONSTRAINT fk_origin_destination_flows_destination_region
        FOREIGN KEY (destination_region_id)
        REFERENCES regions (id),

    CONSTRAINT uk_origin_destination_flows_source_origin_destination_period
        UNIQUE (source_id, origin_region_id, destination_region_id, predominant_period),

    CONSTRAINT ck_origin_destination_flows_origin_cluster_name_not_blank
        CHECK (length(btrim(origin_cluster_name)) > 0),

    CONSTRAINT ck_origin_destination_flows_destination_cluster_name_not_blank
        CHECK (length(btrim(destination_cluster_name)) > 0),

    CONSTRAINT ck_origin_destination_flows_origin_municipality_not_blank
        CHECK (length(btrim(origin_municipality)) > 0),

    CONSTRAINT ck_origin_destination_flows_destination_municipality_not_blank
        CHECK (length(btrim(destination_municipality)) > 0),

    CONSTRAINT ck_origin_destination_flows_origin_latitude_range
        CHECK (origin_latitude BETWEEN -90 AND 90),

    CONSTRAINT ck_origin_destination_flows_origin_longitude_range
        CHECK (origin_longitude BETWEEN -180 AND 180),

    CONSTRAINT ck_origin_destination_flows_destination_latitude_range
        CHECK (destination_latitude BETWEEN -90 AND 90),

    CONSTRAINT ck_origin_destination_flows_destination_longitude_range
        CHECK (destination_longitude BETWEEN -180 AND 180),

    CONSTRAINT ck_origin_destination_flows_users_non_negative
        CHECK (users_count >= 0),

    CONSTRAINT ck_origin_destination_flows_trips_non_negative
        CHECK (trips_count >= 0),

    CONSTRAINT ck_origin_destination_flows_average_distance_non_negative
        CHECK (average_distance_km >= 0),

    CONSTRAINT ck_origin_destination_flows_predominant_period
        CHECK (predominant_period IN ('MADRUGADA', 'MANHA', 'TARDE', 'NOITE'))
);
