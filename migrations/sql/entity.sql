CREATE TABLE IF NOT EXISTS entity
(
    entity_id BIGINT,
    name TEXT,
    ontology TEXT,
    description TEXT,
    attribute BIGINT,

    PRIMARY KEY (entity_id)
)