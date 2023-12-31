package highlaw00.kaunotifier.repository;

import highlaw00.kaunotifier.entity.Source;

import java.util.List;
import java.util.Optional;

public interface SourceRepository {
    Source save(Source source);
    Optional<Source> findById(Long id);
    List<Source> findAll();

}
