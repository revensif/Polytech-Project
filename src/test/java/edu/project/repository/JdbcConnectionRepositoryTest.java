package edu.project.repository;

import edu.project.IntegrationTest;
import edu.project.repository.entity.Connection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class JdbcConnectionRepositoryTest extends IntegrationTest {

    private static final int FIRST_ID = 1;
    private static final int SECOND_ID = 2;
    private static final String FIRST_NAME = "first_name";
    private static final String SECOND_NAME = "second_name";
    private static final String ONTOLOGY = "ontology";
    private static final String TYPE = "type";
    private static final String ATTRIBUTE = "attribute";
    private static final String DESCRIPTION = "description";
    private static final OffsetDateTime DATE_TIME = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Connection FIRST_CONNECTION = new Connection(
            FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, DESCRIPTION, DATE_TIME
    );
    private static final Connection SECOND_CONNECTION = new Connection(
            FIRST_ID, SECOND_ID, FIRST_NAME, SECOND_NAME, DESCRIPTION, DATE_TIME
    );

    @Autowired
    private JdbcEntityRepository entityRepository;

    @Autowired
    private JdbcConnectionRepository connectionRepository;

    @Test
    void shouldAddConnectionToDatabase() {
        //arrange
        prepareDatabase();
        //act + assert
        assertThat(connectionRepository.findAll().size()).isEqualTo(0);
        assertThat(connectionRepository.findAll()).isEmpty();
        assertThat(connectionRepository.addConnection(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, DESCRIPTION, DATE_TIME))
                .isEqualTo(FIRST_CONNECTION);
        assertThat(connectionRepository.findAll().size()).isEqualTo(1);
        assertThat(connectionRepository.findAll()).isEqualTo(List.of(FIRST_CONNECTION));
        assertThat(connectionRepository.addConnection(FIRST_ID, SECOND_ID, FIRST_NAME, SECOND_NAME, DESCRIPTION, DATE_TIME))
                .isEqualTo(SECOND_CONNECTION);
        assertThat(connectionRepository.findAll().size()).isEqualTo(2);
        assertThat(connectionRepository.findAll()).isEqualTo(List.of(FIRST_CONNECTION, SECOND_CONNECTION));
        assertThat(connectionRepository.findConnectionByEntitiesId(FIRST_ID, FIRST_ID)).isEqualTo(FIRST_CONNECTION);
        assertThat(connectionRepository.findConnectionByEntitiesId(FIRST_ID, SECOND_ID)).isEqualTo(SECOND_CONNECTION);
        assertThat(connectionRepository.findConnectionByEntitiesId(2, 2)).isNull();
        assertThrows(DataIntegrityViolationException.class, () ->
                connectionRepository.addConnection(SECOND_ID, 3, FIRST_NAME, FIRST_NAME, DESCRIPTION, DATE_TIME));
    }

    @Test
    void shouldFindAllConnectionsFromDatabase() {
        //arrange
        prepareDatabase();
        //act + assert
        assertThat(connectionRepository.findAll().size()).isEqualTo(0);
        assertThat(connectionRepository.findAll()).isEmpty();
        connectionRepository.addConnection(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, DESCRIPTION, DATE_TIME);
        assertThat(connectionRepository.findAll().size()).isEqualTo(1);
        assertThat(connectionRepository.findAll()).isEqualTo(List.of(FIRST_CONNECTION));
        connectionRepository.addConnection(FIRST_ID, SECOND_ID, FIRST_NAME, SECOND_NAME, DESCRIPTION, DATE_TIME);
        assertThat(connectionRepository.findAll().size()).isEqualTo(2);
        assertThat(connectionRepository.findAll()).isEqualTo(List.of(FIRST_CONNECTION, SECOND_CONNECTION));
    }

    @Test
    void shouldDeleteConnectionFromDatabase() {
        //arrange
        prepareDatabase();
        connectionRepository.addConnection(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, DESCRIPTION, DATE_TIME);
        connectionRepository.addConnection(FIRST_ID, SECOND_ID, FIRST_NAME, SECOND_NAME, DESCRIPTION, DATE_TIME);
        //act + assert
        assertThat(connectionRepository.findAll().size()).isEqualTo(2);
        assertThat(connectionRepository.findAll()).isEqualTo(List.of(FIRST_CONNECTION, SECOND_CONNECTION));
        assertThat(connectionRepository.deleteConnection(FIRST_ID, FIRST_ID))
                .isEqualTo(FIRST_CONNECTION);
        assertThat(connectionRepository.findAll().size()).isEqualTo(1);
        assertThat(connectionRepository.findAll()).isEqualTo(List.of(SECOND_CONNECTION));
        assertThat(connectionRepository.deleteConnection(SECOND_ID, SECOND_ID))
                .isNull();
        assertThat(connectionRepository.findAll().size()).isEqualTo(1);
        assertThat(connectionRepository.findAll()).isEqualTo(List.of(SECOND_CONNECTION));
        assertThat(connectionRepository.deleteConnection(FIRST_ID, SECOND_ID))
                .isEqualTo(SECOND_CONNECTION);
        assertThat(connectionRepository.findAll().size()).isEqualTo(0);
        assertThat(connectionRepository.findAll()).isEmpty();
    }

    @Test
    void shouldFindAllConnectionsByEntityId() {
        //arrange
        prepareDatabase();
        connectionRepository.addConnection(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, DESCRIPTION, DATE_TIME);
        connectionRepository.addConnection(FIRST_ID, SECOND_ID, FIRST_NAME, SECOND_NAME, DESCRIPTION, DATE_TIME);
        //act + assert
        assertThat(connectionRepository.findConnectionByEntitiesId(FIRST_ID, FIRST_ID)).isEqualTo(FIRST_CONNECTION);
        assertThat(connectionRepository.findConnectionByEntitiesId(FIRST_ID, SECOND_ID)).isEqualTo(SECOND_CONNECTION);
        assertThat(connectionRepository.findConnectionByEntitiesId(SECOND_ID, SECOND_ID)).isNull();
    }

    @Test
    void shouldFindAllConnectionsByEntityName() {
        //arrange
        prepareDatabase();
        connectionRepository.addConnection(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, DESCRIPTION, DATE_TIME);
        connectionRepository.addConnection(FIRST_ID, SECOND_ID, FIRST_NAME, SECOND_NAME, DESCRIPTION, DATE_TIME);
        //act + assert
        assertThat(connectionRepository.findConnectionByEntitiesNames(FIRST_NAME, FIRST_NAME)).isEqualTo(FIRST_CONNECTION);
        assertThat(connectionRepository.findConnectionByEntitiesNames(FIRST_NAME, SECOND_NAME)).isEqualTo(SECOND_CONNECTION);
        assertThat(connectionRepository.findConnectionByEntitiesNames(SECOND_NAME, FIRST_NAME)).isNull();
    }

    private void prepareDatabase() {
        entityRepository.addEntity(FIRST_ID, FIRST_NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        entityRepository.addEntity(SECOND_ID, SECOND_NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
    }
}
