package edu.project.repository;

import edu.project.repository.entity.Entity;
import edu.project.repository.mapper.EntityRowMapper;
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
public class JdbcEntityRepository {

    private final JdbcTemplate jdbcTemplate;

    public Entity addEntity(
            int entityId,
            String name,
            String ontology,
            String type,
            String attribute,
            OffsetDateTime createdAt) {
        jdbcTemplate.update("INSERT INTO entity VALUES (?, ?, ?, ?, ?, ?)",
                entityId, name, ontology, type, attribute, createdAt);
        return findById(entityId);
    }

    public Entity deleteEntity(int entityId) {
        Entity removedEntity = findById(entityId);
        jdbcTemplate.update("DELETE FROM entity WHERE entity_id = ?", entityId);
        return removedEntity;
    }

    public Entity findById(int entityId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM entity WHERE entity_id = ?", new EntityRowMapper(), entityId);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<Entity> findByAllFieldsExpectIdAndCreatedAt(String name, String ontology, String type, String attribute) {
        return jdbcTemplate.query(
                "SELECT * FROM entity WHERE name = ? AND ontology = ? AND entity_type = ? AND attribute = ?",
                new EntityRowMapper(), name, ontology, type, attribute);
    }

    public List<Entity> findAll() {
        return jdbcTemplate.query("SELECT * FROM entity", new EntityRowMapper());
    }
}
