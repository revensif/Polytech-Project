CREATE TABLE IF NOT EXISTS parameter
(
    parameter_id BIGSERIAL,
    entity_id BIGSERIAL,
    name TEXT,
    measure_unit TEXT,
    value BIGINT,

    FOREIGN KEY (entity_id) REFERENCES entity ON DELETE CASCADE,
    PRIMARY KEY (parameter_id)
)