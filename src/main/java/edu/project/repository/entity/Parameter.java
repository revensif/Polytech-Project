package edu.project.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parameter {

    private int entityId;
    private String name;
    private String measureUnit;
    private int value;
}
