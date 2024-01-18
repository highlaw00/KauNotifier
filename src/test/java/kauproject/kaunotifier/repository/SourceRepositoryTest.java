package kauproject.kaunotifier.repository;

import jakarta.transaction.Transactional;
import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("")
public class SourceRepositoryTest {

    @Autowired SourceRepository sourceRepository;

    @Test
    void findAll() {
        // given
        List<Source> sourceList = sourceRepository.findAllList();

        assertThat(sourceList.size()).isEqualTo(13);
    }
}
