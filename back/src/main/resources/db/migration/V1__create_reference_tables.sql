CREATE TABLE data_sources (
    id UUID PRIMARY KEY,

    source_name VARCHAR(120) NOT NULL,
    file_name VARCHAR(120) NOT NULL,
    source_type VARCHAR(32) NOT NULL,
    description VARCHAR(500) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ck_data_sources_source_name_not_blank
        CHECK (length(btrim(source_name)) > 0),

    CONSTRAINT ck_data_sources_file_name_length
        CHECK (char_length(file_name) BETWEEN 5 AND 120),

    CONSTRAINT ck_data_sources_file_name_not_blank
        CHECK (length(btrim(file_name)) > 0),

    CONSTRAINT ck_data_sources_file_name_csv_extension
        CHECK (lower(file_name) LIKE '%.csv'),

    CONSTRAINT ck_data_sources_file_name_no_path
        CHECK (
            position('/' IN file_name) = 0
            AND position(chr(92) IN file_name) = 0
            AND position('..' IN file_name) = 0
        ),

    CONSTRAINT ck_data_sources_source_type
        CHECK (
            source_type IN (
                'SYNTHETIC_DATASET',
                'PUBLIC_SOURCE',
                'COMPLEMENTARY_SOURCE',
                'SEED_DATA',
                'ESTIMATED'
            )
        ),

    CONSTRAINT ck_data_sources_description_not_blank
        CHECK (length(btrim(description)) > 0),

    CONSTRAINT uk_data_sources_file_type
        UNIQUE (file_name, source_type)
);

CREATE TABLE regions (
    id UUID PRIMARY KEY,

    region_code VARCHAR(80) NOT NULL,
    region_name VARCHAR(80) NOT NULL,
    municipality VARCHAR(60) NOT NULL,
    cluster_name VARCHAR(40) NOT NULL,
    center_latitude NUMERIC(9, 6) NOT NULL,
    center_longitude NUMERIC(9, 6) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_regions_region_code
        UNIQUE (region_code),

    CONSTRAINT uk_regions_cluster_municipality
        UNIQUE (cluster_name, municipality),

    CONSTRAINT ck_regions_region_code_length
        CHECK (char_length(region_code) BETWEEN 3 AND 80),

    CONSTRAINT ck_regions_region_code_uppercase
        CHECK (region_code = upper(region_code)),

    CONSTRAINT ck_regions_region_code_format
        CHECK (
            region_code !~ '^[^A-Z0-9]'
            AND region_code !~ '[^A-Z0-9]$'
            AND region_code !~ '[-_]{2,}'
            AND region_code !~ '[^A-Z0-9_-]'
        ),

    CONSTRAINT ck_regions_region_name_not_blank
        CHECK (length(btrim(region_name)) > 0),

    CONSTRAINT ck_regions_municipality_not_blank
        CHECK (length(btrim(municipality)) > 0),

    CONSTRAINT ck_regions_cluster_name_not_blank
        CHECK (length(btrim(cluster_name)) > 0),

    CONSTRAINT ck_regions_latitude_range
        CHECK (center_latitude BETWEEN -90 AND 90),

    CONSTRAINT ck_regions_longitude_range
        CHECK (center_longitude BETWEEN -180 AND 180),

    CONSTRAINT ck_regions_coordinates_not_zero_zero
        CHECK (NOT (center_latitude = 0 AND center_longitude = 0))
);

CREATE TABLE antennas (
    id UUID PRIMARY KEY,

    ecgi VARCHAR(16) NOT NULL,
    region_id UUID NOT NULL,
    cluster_name VARCHAR(40) NOT NULL,
    municipality VARCHAR(60) NOT NULL,
    latitude NUMERIC(9, 6) NOT NULL,
    longitude NUMERIC(9, 6) NOT NULL,
    source_id UUID NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_antennas_ecgi
        UNIQUE (ecgi),

    CONSTRAINT fk_antennas_region
        FOREIGN KEY (region_id)
        REFERENCES regions (id),

    CONSTRAINT fk_antennas_source
        FOREIGN KEY (source_id)
        REFERENCES data_sources (id),

    CONSTRAINT ck_antennas_ecgi_length
        CHECK (char_length(ecgi) BETWEEN 12 AND 16),

    CONSTRAINT ck_antennas_ecgi_digits_only
        CHECK (ecgi ~ '^[0-9]+$'),

    CONSTRAINT ck_antennas_cluster_name_not_blank
        CHECK (length(btrim(cluster_name)) > 0),

    CONSTRAINT ck_antennas_municipality_not_blank
        CHECK (length(btrim(municipality)) > 0),

    CONSTRAINT ck_antennas_latitude_range
        CHECK (latitude BETWEEN -90 AND 90),

    CONSTRAINT ck_antennas_longitude_range
        CHECK (longitude BETWEEN -180 AND 180),

    CONSTRAINT ck_antennas_coordinates_not_zero_zero
        CHECK (NOT (latitude = 0 AND longitude = 0))
);
