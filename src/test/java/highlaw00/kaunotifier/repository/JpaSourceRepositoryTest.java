package highlaw00.kaunotifier.repository;

import highlaw00.kaunotifier.entity.Source;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class JpaSourceRepositoryTest {
    private final SourceRepository repository;
    @Autowired
    public JpaSourceRepositoryTest(SourceRepository repository) {
        this.repository = repository;
    }

    @Test
    void findById() {
        Long id = 1L;
        Source source = repository.findById(id).get();

        Assertions.assertThat(id).isEqualTo(source.getSourceId());
    }
    @Test
    void findAll() {
        List<Source> sourceList = repository.findAll();
        for (Source source : sourceList) {
            System.out.println("source.getSourceName() = " + source.getSourceName());
        }
        Assertions.assertThat(sourceList.isEmpty()).isFalse();
    }
}
