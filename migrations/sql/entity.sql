CREATE TABLE IF NOT EXISTS entity
(
    entity_id   BIGINT,
    name        TEXT,
    ontology    TEXT,
    entity_type TEXT,
    attribute   TEXT,
    created_at  TIMESTAMP WITH TIME ZONE,

    PRIMARY KEY (entity_id)
)