package edu.project.repository.mapper;

import edu.project.repository.entity.Entity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityRowMapper implements RowMapper<Entity> {

    @Override
    public Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
        int entityId = rs.getInt("entity_id");
        String name = rs.getString("name");
        String ontology = rs.getString("ontology");
        String description = rs.getString("description");
        int attribute = rs.getInt("attribute");
        return new Entity(entityId, name, ontology, description, attribute);
    }
}
