package edu.project.repository.mapper;

import edu.project.repository.entity.Parameter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParameterRowMapper implements RowMapper<Parameter> {

    @Override
    public Parameter mapRow(ResultSet rs, int rowNum) throws SQLException {
        int entityId = rs.getInt("entity_id");
        String name = rs.getString("name");
        String measureUnit = rs.getString("measure_unit");
        int value = rs.getInt("value");
        return new Parameter(entityId, name, measureUnit, value);
    }
}
