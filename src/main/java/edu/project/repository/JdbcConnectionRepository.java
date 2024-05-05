package edu.project.repository;

import edu.project.repository.entity.Connection;
import edu.project.repository.mapper.ConnectionRowMapper;
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
public class JdbcConnectionRepository {

    private final JdbcTemplate jdbcTemplate;

    public Connection addConnection(
            int firstEntityId,
            int secondEntityId,
            String firstEntityName,
            String secondEntityName,
            String description,
            OffsetDateTime createdAt) {
        jdbcTemplate.update("INSERT INTO connection "
                        + "(first_entity_id, second_entity_id, first_entity_name, second_entity_name, description, created_at) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                firstEntityId, secondEntityId, firstEntityName, secondEntityName, description, createdAt);
        return findConnectionByEntitiesId(firstEntityId, secondEntityId);
    }

    public Connection deleteConnection(int firstEntityId, int secondEntityId) {
        Connection removedConnection = findConnectionByEntitiesId(firstEntityId, secondEntityId);
        jdbcTemplate.update("DELETE FROM connection WHERE first_entity_id = ? AND second_entity_id = ?",
                firstEntityId, secondEntityId);
        return removedConnection;
    }

    public Connection findConnectionByEntitiesId(int firstEntityId, int secondEntityId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM connection WHERE first_entity_id = ? AND second_entity_id = ?",
                    new ConnectionRowMapper(), firstEntityId, secondEntityId);
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<Connection> findAll() {
        return jdbcTemplate.query("SELECT * FROM connection", new ConnectionRowMapper());
    }
}
