package edu.project.repository;

import edu.project.repository.entity.Connection;
import edu.project.repository.entity.MathModel;
import edu.project.repository.mapper.ConnectionRowMapper;
import edu.project.repository.mapper.MathModelRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class JdbcMathModelRepository {

    private final JdbcTemplate jdbcTemplate;

    public MathModel addMathModel(
            int firstEntityId,
            int secondEntityId,
            String firstEntityName,
            String secondEntityName,
            String name,
            String formula,
            OffsetDateTime createdAt
    ) {
        jdbcTemplate.update("INSERT INTO math_model "
                        + "(first_entity_id, second_entity_id, first_entity_name, second_entity_name, name, formula, created_at) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                firstEntityId, secondEntityId, firstEntityName, secondEntityName, name, formula, createdAt);
        return findMathModelByEntitiesId(firstEntityId, secondEntityId);
    }

    public MathModel deleteMathModel(int firstEntityId, int secondEntityId) {
        MathModel removedMathModel = findMathModelByEntitiesId(firstEntityId, secondEntityId);
        jdbcTemplate.update("DELETE FROM math_model WHERE first_entity_id = ? AND second_entity_id = ?",
                firstEntityId, secondEntityId);
        return removedMathModel;
    }

    public MathModel findMathModelByEntitiesId(int firstEntityId, int secondEntityId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM math_model WHERE first_entity_id = ? AND second_entity_id = ?",
                    new MathModelRowMapper(), firstEntityId, secondEntityId);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public MathModel findMathModelByEntitiesNames(String firstEntityName, String secondEntityName) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM math_model WHERE first_entity_name = ? AND second_entity_name = ?",
                    new MathModelRowMapper(), firstEntityName, secondEntityName);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<MathModel> findAll() {
        return jdbcTemplate.query("SELECT * FROM math_model", new MathModelRowMapper());
    }
}
