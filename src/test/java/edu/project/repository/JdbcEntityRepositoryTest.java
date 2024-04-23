package edu.project.repository;

import edu.project.IntegrationTest;
import edu.project.repository.entity.Parameter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JdbcEntityRepositoryTest extends IntegrationTest {

    private static final int FIRST_ID = 1;
    private static final int SECOND_ID = 2;
    private static final String NAME = "name";
    private static final String ONTOLOGY = "ontology";
    private static final String DESCRIPTION = "description";
    private static final int ATTRIBUTE = 12;
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
        //act
        parameterRepository.addParameter(FIRST_ID, NAME, MEASURE_UNIT, VALUE);
        parameterRepository.addParameter(SECOND_ID, NAME, MEASURE_UNIT, VALUE);
        //assert
        assertThat(parameterRepository.findAll().size()).isEqualTo(2);
        assertThat(parameterRepository.findAll()).isEqualTo(List.of(FIRST_PARAMETER, SECOND_PARAMETER));
        assertThat(parameterRepository.findAllParametersByEntityId(FIRST_ID)).isEqualTo(List.of(FIRST_PARAMETER));
        assertThat(parameterRepository.findAllParametersByEntityId(SECOND_ID)).isEqualTo(List.of(SECOND_PARAMETER));
        assertThat(parameterRepository.findAllParametersByEntityId(VALUE)).isEmpty();
    }

    @Test
    void shouldFindAllParametersFromDatabase() {

    }

    private void prepareDatabase() {
        entityRepository.addEntity(FIRST_ID, NAME, ONTOLOGY, DESCRIPTION, ATTRIBUTE);
        entityRepository.addEntity(SECOND_ID, NAME, ONTOLOGY, DESCRIPTION, ATTRIBUTE);
    }
}
