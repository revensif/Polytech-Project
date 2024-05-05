CREATE TABLE IF NOT EXISTS connection
(
    connection_id      BIGSERIAL,
    first_entity_id    BIGINT,
    second_entity_id   BIGINT,
    first_entity_name  TEXT,
    second_entity_name TEXT,
    description        TEXT,
    created_at         TIMESTAMP WITH TIME ZONE,

    FOREIGN KEY (first_entity_id) REFERENCES entity ON DELETE CASCADE,
    FOREIGN KEY (second_entity_id) REFERENCES entity ON DELETE CASCADE,
    PRIMARY KEY (connection_id)
)