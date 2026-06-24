CREATE INDEX idx_regions_municipality
    ON regions (municipality);

CREATE INDEX idx_regions_cluster_name
    ON regions (cluster_name);

CREATE INDEX idx_antennas_region_id
    ON antennas (region_id);

CREATE INDEX idx_antennas_source_id
    ON antennas (source_id);

CREATE INDEX idx_antennas_cluster_municipality
    ON antennas (cluster_name, municipality);

CREATE INDEX idx_ingestion_runs_source_id
    ON ingestion_runs (source_id);

CREATE INDEX idx_ingestion_runs_status_started_at
    ON ingestion_runs (status, started_at DESC);

CREATE INDEX idx_concentration_metrics_region_day_period
    ON concentration_metrics (region_id, day_date, period);

CREATE INDEX idx_concentration_metrics_ecgi_day_period
    ON concentration_metrics (ecgi, day_date, period);

CREATE INDEX idx_concentration_metrics_source_id
    ON concentration_metrics (source_id);

CREATE INDEX idx_concentration_metrics_active_users
    ON concentration_metrics (active_users DESC);

CREATE INDEX idx_network_indicators_region_type_period
    ON network_indicators (region_id, indicator_type, period);

CREATE INDEX idx_network_indicators_source_id
    ON network_indicators (source_id);

CREATE INDEX idx_network_indicators_gap_level
    ON network_indicators (gap_level);

CREATE INDEX idx_social_indicators_region_type
    ON social_indicators (region_id, indicator_type);

CREATE INDEX idx_social_indicators_gap_level
    ON social_indicators (gap_level);

CREATE INDEX idx_social_indicators_source_id
    ON social_indicators (source_id);

CREATE INDEX idx_mobility_flows_origin_region
    ON mobility_flows (origin_region_id);

CREATE INDEX idx_mobility_flows_destination_region
    ON mobility_flows (destination_region_id);

CREATE INDEX idx_mobility_flows_origin_ecgi
    ON mobility_flows (origin_ecgi);

CREATE INDEX idx_mobility_flows_destination_ecgi
    ON mobility_flows (destination_ecgi);

CREATE INDEX idx_mobility_flows_users_count
    ON mobility_flows (users_count DESC);

CREATE INDEX idx_mobility_flows_transitions_count
    ON mobility_flows (transitions_count DESC);

CREATE INDEX idx_origin_destination_flows_origin_region
    ON origin_destination_flows (origin_region_id);

CREATE INDEX idx_origin_destination_flows_destination_region
    ON origin_destination_flows (destination_region_id);

CREATE INDEX idx_origin_destination_flows_users_count
    ON origin_destination_flows (users_count DESC);

CREATE INDEX idx_origin_destination_flows_trips_count
    ON origin_destination_flows (trips_count DESC);

CREATE INDEX idx_travel_distance_metrics_origin_region
    ON travel_distance_metrics (origin_region_id);

CREATE INDEX idx_travel_distance_metrics_destination_region
    ON travel_distance_metrics (destination_region_id);

CREATE INDEX idx_travel_distance_metrics_average_distance
    ON travel_distance_metrics (average_distance_km DESC);

CREATE INDEX idx_privacy_summaries_source_id
    ON privacy_summaries (source_id);

CREATE INDEX idx_ai_queries_created_at
    ON ai_queries (created_at DESC);

CREATE INDEX idx_ai_queries_status
    ON ai_queries (status);

CREATE INDEX idx_ai_queries_intent
    ON ai_queries (intent);

CREATE INDEX idx_ai_queries_filters_gin
    ON ai_queries USING GIN (filters);

CREATE INDEX idx_ai_answers_query_id
    ON ai_answers (query_id);

CREATE INDEX idx_ai_answer_evidence_answer_id
    ON ai_answer_evidence (answer_id);

CREATE INDEX idx_ai_answer_regions_region_id
    ON ai_answer_regions (region_id);

CREATE INDEX idx_ai_answer_sources_source_id
    ON ai_answer_sources (source_id);
