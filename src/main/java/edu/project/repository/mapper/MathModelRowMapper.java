package edu.project.repository.mapper;

import edu.project.repository.entity.MathModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class MathModelRowMapper implements RowMapper<MathModel> {

    private static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    @Override
    public MathModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        int firstEntityId = rs.getInt("first_entity_id");
        int secondEntityId = rs.getInt("second_entity_id");
        String firstEntityName = rs.getString("first_entity_name");
        String secondEntityName = rs.getString("second_entity_name");
        String name = rs.getString("name");
        String formula = rs.getString("formula");
        OffsetDateTime createdAt = rs.getTimestamp("created_at")
                .toLocalDateTime()
                .atOffset(ZONE_OFFSET);
        return new MathModel(firstEntityId, secondEntityId, firstEntityName, secondEntityName, name, formula, createdAt);
    }
}
