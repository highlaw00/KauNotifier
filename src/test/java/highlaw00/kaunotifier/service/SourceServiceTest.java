package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.Source;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
public class SourceServiceTest {
    private final SourceService sourceService;
    @Autowired
    public SourceServiceTest(SourceService sourceService) {
        this.sourceService = sourceService;
    }
    @Test
    void getSpecificSourceMap() {
        //given
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(10L);

        //when
        List<Source> selectedSources = sourceService.getSpecificSourcesMap(ids);

        //then
        System.out.println("selectedSources.get(0).getDescription() = " + selectedSources.get(0).getDescription());
        Assertions.assertThat(ids.size()).isEqualTo(selectedSources.size());
    }
}
