package edu.project.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {

    private int entityId;
    private String name;
    private String measureUnit;
    private int value;
    private int iteration;
    private OffsetDateTime createdAt;
}
