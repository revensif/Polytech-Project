package edu.project.repository;

import edu.project.repository.entity.Parameter;
import edu.project.repository.mapper.ParameterRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcParameterRepository {

    private final JdbcTemplate jdbcTemplate;

    public Parameter addParameter(int entityId, String name, String measureUnit, int value) {
        jdbcTemplate.update("INSERT INTO parameter (entity_id, name, measure_unit, value) VALUES (?, ?, ?, ?)",
                entityId, name, measureUnit, value);
        return findParameterByAllFieldsExceptParameterId(entityId, name, measureUnit, value).getFirst();
    }

    public Parameter deleteParameter(int entityId, String name, String measureUnit, int value) {
        Parameter removedParameter = findParameterByAllFieldsExceptParameterId(entityId, name, measureUnit, value).getFirst();
        jdbcTemplate.update(
                "DELETE FROM parameter WHERE entity_id = ? AND name = ? AND measure_unit = ? AND value = ?",
                entityId, name, measureUnit, value);
        return removedParameter;
    }

    public List<Parameter> findParameterByAllFieldsExceptParameterId(int entityId, String name, String measureUnit, int value) {
        return jdbcTemplate.query(
                "SELECT * FROM parameter WHERE entity_id = ? AND name = ? AND measure_unit = ? AND value = ?",
                new ParameterRowMapper(), entityId, name, measureUnit, value);
    }

    public List<Parameter> findAll() {
        return jdbcTemplate.query("SELECT * FROM parameter", new ParameterRowMapper());
    }

    public List<Parameter> findAllParametersByEntityId(int entityId) {
        return jdbcTemplate.query("SELECT * FROM parameter WHERE entity_id = ?", new ParameterRowMapper(), entityId);
    }
}
