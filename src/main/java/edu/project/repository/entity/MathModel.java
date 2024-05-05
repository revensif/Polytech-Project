package edu.project.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MathModel {

    private int firstEntityId;
    private int secondEntityId;
    private String firstEntityName;
    private String secondEntityName;
    private String name;
    private String formula;
    private OffsetDateTime createdAt;
}
