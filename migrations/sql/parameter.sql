CREATE TABLE IF NOT EXISTS parameter
(
    parameter_id BIGSERIAL,
    entity_id    BIGINT,
    name         TEXT,
    measure_unit TEXT,
    value        BIGINT,
    iteration    BIGINT,
    created_at   TIMESTAMP WITH TIME ZONE,

    FOREIGN KEY (entity_id) REFERENCES entity ON DELETE CASCADE,
    PRIMARY KEY (parameter_id)
)