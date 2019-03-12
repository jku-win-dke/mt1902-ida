package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.configuration.DataConfiguration;
import at.jku.dke.inga.data.models.Label;
import at.jku.dke.inga.shared.SharedSpringConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SharedSpringConfiguration.class, DataConfiguration.class})
@ExtendWith(SpringExtension.class)
class CubeRepositoryTest {
    @Autowired
    private CubeRepository repository;

    @Test
    void testFindByLang() {
        // Prepare

        // Execute
        List<Label> labels = repository.findLabelsByLang("en");

        // Assert
        assertEquals(1, labels.size());
    }
}
