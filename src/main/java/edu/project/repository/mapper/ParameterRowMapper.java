package edu.project.repository.mapper;

import edu.project.repository.entity.Parameter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ParameterRowMapper implements RowMapper<Parameter> {

    private static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    @Override
    public Parameter mapRow(ResultSet rs, int rowNum) throws SQLException {
        int entityId = rs.getInt("entity_id");
        String name = rs.getString("name");
        String measureUnit = rs.getString("measure_unit");
        int value = rs.getInt("value");
        int iteration = rs.getInt("iteration");
        OffsetDateTime createdAt = rs.getTimestamp("created_at")
                .toLocalDateTime()
                .atOffset(ZONE_OFFSET);
        return new Parameter(entityId, name, measureUnit, value, iteration, createdAt);
    }
}
