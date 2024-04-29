package edu.project.repository;

import edu.project.IntegrationTest;
import edu.project.repository.entity.Parameter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class JdbcParameterRepositoryTest extends IntegrationTest {

    private static final int FIRST_ID = 1;
    private static final int SECOND_ID = 2;
    private static final String NAME = "name";
    private static final String ONTOLOGY = "ontology";
    private static final String DESCRIPTION = "description";
    private static final String ATTRIBUTE = "attribute";
    private static final String MEASURE_UNIT = "unit";
    private static final int VALUE = 7;
    private static final Parameter FIRST_PARAMETER = new Parameter(FIRST_ID, NAME, MEASURE_UNIT, VALUE);
    private static final Parameter SECOND_PARAMETER = new Parameter(SECOND_ID, NAME, MEASURE_UNIT, VALUE);

    @Autowired
    private JdbcEntityRepository entityRepository;

    @Autowired
    private JdbcParameterRepository parameterRepository;

    @Test
    void shouldAddParameterToDatabase() {
        //arrange
        prepareDatabase();
        //act + assert
        assertThat(parameterRepository.findAll().size()).isEqualTo(0);
        assertThat(parameterRepository.findAll()).isEmpty();
        assertThat(parameterRepository.addParameter(FIRST_ID, NAME, MEASURE_UNIT, VALUE)).isEqualTo(FIRST_PARAMETER);
        assertThat(parameterRepository.findAll().size()).isEqualTo(1);
        assertThat(parameterRepository.findAll()).isEqualTo(List.of(FIRST_PARAMETER));
        assertThat(parameterRepository.addParameter(SECOND_ID, NAME, MEASURE_UNIT, VALUE)).isEqualTo(SECOND_PARAMETER);
        assertThat(parameterRepository.findAll().size()).isEqualTo(2);
        assertThat(parameterRepository.findAll()).isEqualTo(List.of(FIRST_PARAMETER, SECOND_PARAMETER));
        assertThat(parameterRepository.findAllParametersByEntityId(FIRST_ID)).isEqualTo(List.of(FIRST_PARAMETER));
        assertThat(parameterRepository.findAllParametersByEntityId(SECOND_ID)).isEqualTo(List.of(SECOND_PARAMETER));
        assertThat(parameterRepository.findAllParametersByEntityId(VALUE)).isEmpty();
        assertThrows(DataIntegrityViolationException.class, () ->
                parameterRepository.addParameter(3, NAME, MEASURE_UNIT, VALUE));
    }

    @Test
    void shouldFindAllParametersFromDatabase() {
        //arrange
        prepareDatabase();
        //act + assert
        assertThat(parameterRepository.findAll().size()).isEqualTo(0);
        assertThat(parameterRepository.findAll()).isEmpty();
        parameterRepository.addParameter(FIRST_ID, NAME, MEASURE_UNIT, VALUE);
        assertThat(parameterRepository.findAll().size()).isEqualTo(1);
        assertThat(parameterRepository.findAll()).isEqualTo(List.of(FIRST_PARAMETER));
        parameterRepository.addParameter(SECOND_ID, NAME, MEASURE_UNIT, VALUE);
        assertThat(parameterRepository.findAll().size()).isEqualTo(2);
        assertThat(parameterRepository.findAll()).isEqualTo(List.of(FIRST_PARAMETER, SECOND_PARAMETER));
    }

    @Test
    void shouldDeleteParameterFromDatabase() {
        //arrange
        prepareDatabase();
        parameterRepository.addParameter(FIRST_ID, NAME, MEASURE_UNIT, VALUE);
        parameterRepository.addParameter(SECOND_ID, NAME, MEASURE_UNIT, VALUE);
        //act + assert
        assertThat(parameterRepository.findAll().size()).isEqualTo(2);
        assertThat(parameterRepository.findAll()).isEqualTo(List.of(FIRST_PARAMETER, SECOND_PARAMETER));
        assertThat(parameterRepository.deleteParameter(FIRST_ID, NAME, MEASURE_UNIT, VALUE)).isEqualTo(FIRST_PARAMETER);
        assertThat(parameterRepository.findAll().size()).isEqualTo(1);
        assertThat(parameterRepository.findAll()).isEqualTo(List.of(SECOND_PARAMETER));
        assertThat(parameterRepository.deleteParameter(3, NAME, MEASURE_UNIT, VALUE)).isNull();
        assertThat(parameterRepository.findAll().size()).isEqualTo(1);
        assertThat(parameterRepository.findAll()).isEqualTo(List.of(SECOND_PARAMETER));
        assertThat(parameterRepository.deleteParameter(SECOND_ID, NAME, MEASURE_UNIT, VALUE)).isEqualTo(SECOND_PARAMETER);
        assertThat(parameterRepository.findAll().size()).isEqualTo(0);
        assertThat(parameterRepository.findAll()).isEmpty();
    }

    @Test
    void shouldFindParameterByAllFieldsExceptParameterId() {
        //arrange
        prepareDatabase();
        parameterRepository.addParameter(FIRST_ID, NAME, MEASURE_UNIT, VALUE);
        parameterRepository.addParameter(SECOND_ID, NAME, MEASURE_UNIT, VALUE);
        //act + assert
        assertThat(parameterRepository.findParameterByAllFieldsExceptParameterId(FIRST_ID, NAME, MEASURE_UNIT, VALUE))
                .isEqualTo(List.of(FIRST_PARAMETER));
        assertThat(parameterRepository.findParameterByAllFieldsExceptParameterId(SECOND_ID, NAME, MEASURE_UNIT, VALUE))
                .isEqualTo(List.of(SECOND_PARAMETER));
        assertThat(parameterRepository.findParameterByAllFieldsExceptParameterId(3, NAME, MEASURE_UNIT, VALUE))
                .isEmpty();
    }

    @Test
    void shouldFindAllParametersByEntityId() {
        //arrange
        prepareDatabase();
        parameterRepository.addParameter(FIRST_ID, NAME, MEASURE_UNIT, VALUE);
        parameterRepository.addParameter(SECOND_ID, NAME, MEASURE_UNIT, VALUE);
        parameterRepository.addParameter(FIRST_ID, NAME + NAME, MEASURE_UNIT, VALUE);
        //act + assert
        assertThat(parameterRepository.findAllParametersByEntityId(FIRST_ID)).isEqualTo(
                List.of(FIRST_PARAMETER, new Parameter(FIRST_ID, NAME + NAME, MEASURE_UNIT, VALUE))
        );
        assertThat(parameterRepository.findAllParametersByEntityId(SECOND_ID)).isEqualTo(List.of(SECOND_PARAMETER));
        assertThat(parameterRepository.findAllParametersByEntityId(3)).isEmpty();
    }

    private void prepareDatabase() {
        entityRepository.addEntity(FIRST_ID, NAME, ONTOLOGY, DESCRIPTION, ATTRIBUTE);
        entityRepository.addEntity(SECOND_ID, NAME, ONTOLOGY, DESCRIPTION, ATTRIBUTE);
    }
}
