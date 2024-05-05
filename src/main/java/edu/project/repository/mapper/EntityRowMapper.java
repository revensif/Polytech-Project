package edu.project.repository.mapper;

import edu.project.repository.entity.Entity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class EntityRowMapper implements RowMapper<Entity> {

    private static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    @Override
    public Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
        int entityId = rs.getInt("entity_id");
        String name = rs.getString("name");
        String ontology = rs.getString("ontology");
        String type = rs.getString("entity_type");
        String attribute = rs.getString("attribute");
        OffsetDateTime createdAt = rs.getTimestamp("created_at")
                .toLocalDateTime()
                .atOffset(ZONE_OFFSET);
        return new Entity(entityId, name, ontology, type, attribute, createdAt);
    }
}
