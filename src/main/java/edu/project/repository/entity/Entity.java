package edu.project.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity {

    private int entityId;
    private String name;
    private String ontology;
    private String description;
    private String attribute;
    private OffsetDateTime createdAt;
}
