CREATE TABLE mobility_flows (
    id UUID PRIMARY KEY,

    source_id UUID NOT NULL,

    origin_region_id UUID NOT NULL,
    destination_region_id UUID NOT NULL,

    origin_ecgi VARCHAR(16) NOT NULL,
    destination_ecgi VARCHAR(16) NOT NULL,

    origin_latitude NUMERIC(9, 6) NOT NULL,
    origin_longitude NUMERIC(9, 6) NOT NULL,
    destination_latitude NUMERIC(9, 6) NOT NULL,
    destination_longitude NUMERIC(9, 6) NOT NULL,

    origin_cluster_name VARCHAR(40) NOT NULL,
    destination_cluster_name VARCHAR(40) NOT NULL,
    origin_municipality VARCHAR(60) NOT NULL,
    destination_municipality VARCHAR(60) NOT NULL,

    users_count BIGINT NOT NULL DEFAULT 0,
    transitions_count BIGINT NOT NULL DEFAULT 0,
    distance_km NUMERIC(10, 3) NOT NULL,
    predominant_period VARCHAR(16) NOT NULL,
    origin_cluster_percentage NUMERIC(6, 3) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_mobility_flows_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT fk_mobility_flows_origin_region
        FOREIGN KEY (origin_region_id)
        REFERENCES regions (id),

    CONSTRAINT fk_mobility_flows_destination_region
        FOREIGN KEY (destination_region_id)
        REFERENCES regions (id),

    CONSTRAINT fk_mobility_flows_origin_ecgi
        FOREIGN KEY (origin_ecgi)
        REFERENCES antennas (ecgi),

    CONSTRAINT fk_mobility_flows_destination_ecgi
        FOREIGN KEY (destination_ecgi)
        REFERENCES antennas (ecgi),

    CONSTRAINT uk_mobility_flows_source_origin_destination_period
        UNIQUE (source_id, origin_ecgi, destination_ecgi, predominant_period),

    CONSTRAINT ck_mobility_flows_origin_ecgi_length
        CHECK (char_length(origin_ecgi) BETWEEN 12 AND 16),

    CONSTRAINT ck_mobility_flows_origin_ecgi_digits_only
        CHECK (origin_ecgi ~ '^[0-9]+$'),

    CONSTRAINT ck_mobility_flows_destination_ecgi_length
        CHECK (char_length(destination_ecgi) BETWEEN 12 AND 16),

    CONSTRAINT ck_mobility_flows_destination_ecgi_digits_only
        CHECK (destination_ecgi ~ '^[0-9]+$'),

    CONSTRAINT ck_mobility_flows_different_ecgi
        CHECK (origin_ecgi <> destination_ecgi),

    CONSTRAINT ck_mobility_flows_origin_latitude_range
        CHECK (origin_latitude BETWEEN -90 AND 90),

    CONSTRAINT ck_mobility_flows_origin_longitude_range
        CHECK (origin_longitude BETWEEN -180 AND 180),

    CONSTRAINT ck_mobility_flows_destination_latitude_range
        CHECK (destination_latitude BETWEEN -90 AND 90),

    CONSTRAINT ck_mobility_flows_destination_longitude_range
        CHECK (destination_longitude BETWEEN -180 AND 180),

    CONSTRAINT ck_mobility_flows_origin_cluster_name_not_blank
        CHECK (length(btrim(origin_cluster_name)) > 0),

    CONSTRAINT ck_mobility_flows_destination_cluster_name_not_blank
        CHECK (length(btrim(destination_cluster_name)) > 0),

    CONSTRAINT ck_mobility_flows_origin_municipality_not_blank
        CHECK (length(btrim(origin_municipality)) > 0),

    CONSTRAINT ck_mobility_flows_destination_municipality_not_blank
        CHECK (length(btrim(destination_municipality)) > 0),

    CONSTRAINT ck_mobility_flows_users_non_negative
        CHECK (users_count >= 0),

    CONSTRAINT ck_mobility_flows_transitions_non_negative
        CHECK (transitions_count >= 0),

    CONSTRAINT ck_mobility_flows_users_not_greater_than_transitions
        CHECK (users_count <= transitions_count),

    CONSTRAINT ck_mobility_flows_distance_non_negative
        CHECK (distance_km >= 0),

    CONSTRAINT ck_mobility_flows_predominant_period
        CHECK (predominant_period IN ('MADRUGADA', 'MANHA', 'TARDE', 'NOITE')),

    CONSTRAINT ck_mobility_flows_origin_cluster_percentage_range
        CHECK (origin_cluster_percentage BETWEEN 0 AND 100)
);
