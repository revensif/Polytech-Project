package edu.project.repository;

import edu.project.IntegrationTest;
import edu.project.repository.entity.Entity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JdbcEntityRepositoryTest extends IntegrationTest {

    private static final int FIRST_ID = 1;
    private static final int SECOND_ID = 2;
    private static final String NAME = "name";
    private static final String ONTOLOGY = "ontology";
    private static final String TYPE = "type";
    private static final String ATTRIBUTE = "attribute";
    private static final OffsetDateTime DATE_TIME = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Entity FIRST_ENTITY = new Entity(FIRST_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
    private static final Entity SECOND_ENTITY = new Entity(SECOND_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);

    @Autowired
    private JdbcEntityRepository entityRepository;

    @Test
    void shouldAddEntityToDatabase() {
        //arrange + act + assert
        assertThat(entityRepository.findAll().size()).isEqualTo(0);
        assertThat(entityRepository.findAll()).isEmpty();
        assertThat(entityRepository.addEntity(FIRST_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME))
                .isEqualTo(FIRST_ENTITY);
        assertThat(entityRepository.findAll().size()).isEqualTo(1);
        assertThat(entityRepository.findAll()).isEqualTo(List.of(FIRST_ENTITY));
        assertThat(entityRepository.addEntity(SECOND_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME))
                .isEqualTo(SECOND_ENTITY);
        assertThat(entityRepository.findAll().size()).isEqualTo(2);
        assertThat(entityRepository.findAll()).isEqualTo(List.of(FIRST_ENTITY, SECOND_ENTITY));
    }

    @Test
    void shouldDeleteEntityFromDatabase() {
        //arrange
        entityRepository.addEntity(FIRST_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        entityRepository.addEntity(SECOND_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        //act + assert
        assertThat(entityRepository.findAll().size()).isEqualTo(2);
        assertThat(entityRepository.findAll()).isEqualTo(List.of(FIRST_ENTITY, SECOND_ENTITY));
        assertThat(entityRepository.deleteEntity(FIRST_ID)).isEqualTo(FIRST_ENTITY);
        assertThat(entityRepository.findAll().size()).isEqualTo(1);
        assertThat(entityRepository.findAll()).isEqualTo(List.of(SECOND_ENTITY));
        assertThat(entityRepository.deleteEntity(3)).isNull();
        assertThat(entityRepository.findAll().size()).isEqualTo(1);
        assertThat(entityRepository.findAll()).isEqualTo(List.of(SECOND_ENTITY));
        assertThat(entityRepository.deleteEntity(SECOND_ID)).isEqualTo(SECOND_ENTITY);
        assertThat(entityRepository.findAll().size()).isEqualTo(0);
        assertThat(entityRepository.findAll()).isEmpty();
    }

    @Test
    void shouldFindById() {
        //arrange + act + assert
        assertThat(entityRepository.findById(FIRST_ID)).isNull();
        entityRepository.addEntity(FIRST_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        assertThat(entityRepository.findById(FIRST_ID)).isEqualTo(FIRST_ENTITY);
        entityRepository.addEntity(SECOND_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        assertThat(entityRepository.findById(SECOND_ID)).isEqualTo(SECOND_ENTITY);
    }

    @Test
    void shouldFindAllEntities() {
        //arrange + act + assert
        assertThat(entityRepository.findAll()).isEmpty();
        entityRepository.addEntity(FIRST_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        assertThat(entityRepository.findAll().size()).isEqualTo(1);
        assertThat(entityRepository.findAll()).isEqualTo(List.of(FIRST_ENTITY));
        entityRepository.addEntity(SECOND_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        assertThat(entityRepository.findAll().size()).isEqualTo(2);
        assertThat(entityRepository.findAll()).isEqualTo(List.of(FIRST_ENTITY, SECOND_ENTITY));
    }

    @Test
    void shouldFindEntityByAllFieldsExceptIdAndCreatedAt() {
        //arrange
        entityRepository.addEntity(FIRST_ID, NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        entityRepository.addEntity(SECOND_ID, NAME, ONTOLOGY, TYPE + "1", ATTRIBUTE, DATE_TIME);
        //act + assert
        assertThat(entityRepository.findByAllFieldsExpectIdAndCreatedAt(NAME, ONTOLOGY, TYPE, ATTRIBUTE))
                .isEqualTo(List.of(FIRST_ENTITY));
        assertThat(entityRepository.findByAllFieldsExpectIdAndCreatedAt(NAME, ONTOLOGY + "1", TYPE, ATTRIBUTE))
                .isEmpty();
    }
}
