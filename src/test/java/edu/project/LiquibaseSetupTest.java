package edu.project;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LiquibaseSetupTest extends IntegrationTest {

    private static final List<String> EXPECTED_TABLES = List.of("entity", "parameter");

    @Test
    public void shouldGetAllTablesInDatabase() throws SQLException {
        //arrange
        Connection connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
        );
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(
                null,
                null,
                null,
                new String[]{"TABLE"}
        );
        List<String> actualTables = new ArrayList<>();
        //act
        while (resultSet.next()) {
            actualTables.add(resultSet.getString("TABLE_NAME"));
        }
        //assert
        assertThat(connection).isNotNull();
        assertThat(connection.isClosed()).isFalse();
        assertThat(actualTables).containsAll(EXPECTED_TABLES);
    }
}
