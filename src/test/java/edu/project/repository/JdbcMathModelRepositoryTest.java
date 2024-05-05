package edu.project.repository;

import edu.project.IntegrationTest;
import edu.project.repository.entity.MathModel;
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
public class JdbcMathModelRepositoryTest extends IntegrationTest {

    private static final int FIRST_ID = 1;
    private static final int SECOND_ID = 2;
    private static final String FIRST_NAME = "first_name";
    private static final String SECOND_NAME = "second_name";
    private static final String ONTOLOGY = "ontology";
    private static final String TYPE = "type";
    private static final String ATTRIBUTE = "attribute";
    private static final String NAME = "name";
    private static final String FORMULA = "formula";
    private static final OffsetDateTime DATE_TIME = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    private static final MathModel FIRST_MATH_MODEL = new MathModel(
            FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME
    );
    private static final MathModel SECOND_MATH_MODEL = new MathModel(
            FIRST_ID, SECOND_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME
    );

    @Autowired
    private JdbcEntityRepository entityRepository;

    @Autowired
    private JdbcMathModelRepository mathModelRepository;

    @Test
    void shouldAddConnectionToDatabase() {
        //arrange
        prepareDatabase();
        //act + assert
        assertThat(mathModelRepository.findAll().size()).isEqualTo(0);
        assertThat(mathModelRepository.findAll()).isEmpty();
        assertThat(mathModelRepository.addMathModel(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME))
                .isEqualTo(FIRST_MATH_MODEL);
        assertThat(mathModelRepository.findAll().size()).isEqualTo(1);
        assertThat(mathModelRepository.findAll()).isEqualTo(List.of(FIRST_MATH_MODEL));
        assertThat(mathModelRepository.addMathModel(FIRST_ID, SECOND_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME))
                .isEqualTo(SECOND_MATH_MODEL);
        assertThat(mathModelRepository.findAll().size()).isEqualTo(2);
        assertThat(mathModelRepository.findAll()).isEqualTo(List.of(FIRST_MATH_MODEL, SECOND_MATH_MODEL));
        assertThat(mathModelRepository.findMathModelByEntitiesId(FIRST_ID, FIRST_ID)).isEqualTo(FIRST_MATH_MODEL);
        assertThat(mathModelRepository.findMathModelByEntitiesId(FIRST_ID, SECOND_ID)).isEqualTo(SECOND_MATH_MODEL);
        assertThat(mathModelRepository.findMathModelByEntitiesId(2, 2)).isNull();
        assertThrows(DataIntegrityViolationException.class, () ->
                mathModelRepository.addMathModel(SECOND_ID, 3, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME));
    }

    @Test
    void shouldFindAllConnectionsFromDatabase() {
        //arrange
        prepareDatabase();
        //act + assert
        assertThat(mathModelRepository.findAll().size()).isEqualTo(0);
        assertThat(mathModelRepository.findAll()).isEmpty();
        mathModelRepository.addMathModel(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME);
        assertThat(mathModelRepository.findAll().size()).isEqualTo(1);
        assertThat(mathModelRepository.findAll()).isEqualTo(List.of(FIRST_MATH_MODEL));
        mathModelRepository.addMathModel(FIRST_ID, SECOND_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME);
        assertThat(mathModelRepository.findAll().size()).isEqualTo(2);
        assertThat(mathModelRepository.findAll()).isEqualTo(List.of(FIRST_MATH_MODEL, SECOND_MATH_MODEL));
    }

    @Test
    void shouldDeleteConnectionFromDatabase() {
        //arrange
        prepareDatabase();
        mathModelRepository.addMathModel(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME);
        mathModelRepository.addMathModel(FIRST_ID, SECOND_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME);
        //act + assert
        assertThat(mathModelRepository.findAll().size()).isEqualTo(2);
        assertThat(mathModelRepository.findAll()).isEqualTo(List.of(FIRST_MATH_MODEL, SECOND_MATH_MODEL));
        assertThat(mathModelRepository.deleteMathModel(FIRST_ID, FIRST_ID))
                .isEqualTo(FIRST_MATH_MODEL);
        assertThat(mathModelRepository.findAll().size()).isEqualTo(1);
        assertThat(mathModelRepository.findAll()).isEqualTo(List.of(SECOND_MATH_MODEL));
        assertThat(mathModelRepository.deleteMathModel(SECOND_ID, SECOND_ID))
                .isNull();
        assertThat(mathModelRepository.findAll().size()).isEqualTo(1);
        assertThat(mathModelRepository.findAll()).isEqualTo(List.of(SECOND_MATH_MODEL));
        assertThat(mathModelRepository.deleteMathModel(FIRST_ID, SECOND_ID))
                .isEqualTo(SECOND_MATH_MODEL);
        assertThat(mathModelRepository.findAll().size()).isEqualTo(0);
        assertThat(mathModelRepository.findAll()).isEmpty();
    }

    @Test
    void shouldFindAllParametersByEntityId() {
        //arrange
        prepareDatabase();
        mathModelRepository.addMathModel(FIRST_ID, FIRST_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME);
        mathModelRepository.addMathModel(FIRST_ID, SECOND_ID, FIRST_NAME, FIRST_NAME, NAME, FORMULA, DATE_TIME);
        //act + assert
        assertThat(mathModelRepository.findMathModelByEntitiesId(FIRST_ID, FIRST_ID)).isEqualTo(FIRST_MATH_MODEL);
        assertThat(mathModelRepository.findMathModelByEntitiesId(FIRST_ID, SECOND_ID)).isEqualTo(SECOND_MATH_MODEL);
        assertThat(mathModelRepository.findMathModelByEntitiesId(SECOND_ID, SECOND_ID)).isNull();
    }

    private void prepareDatabase() {
        entityRepository.addEntity(FIRST_ID, FIRST_NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
        entityRepository.addEntity(SECOND_ID, SECOND_NAME, ONTOLOGY, TYPE, ATTRIBUTE, DATE_TIME);
    }
}
