CREATE TABLE IF NOT EXISTS math_model
(
    math_model_id      BIGSERIAL,
    first_entity_id    BIGINT,
    second_entity_id   BIGINT,
    first_entity_name  TEXT,
    second_entity_name TEXT,
    name               TEXT,
    formula            TEXT,
    created_at         TIMESTAMP WITH TIME ZONE,

    FOREIGN KEY (first_entity_id) REFERENCES entity ON DELETE CASCADE,
    FOREIGN KEY (second_entity_id) REFERENCES entity ON DELETE CASCADE,
    PRIMARY KEY (math_model_id)
)