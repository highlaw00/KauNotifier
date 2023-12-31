package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.Source;
import highlaw00.kaunotifier.repository.SourceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
public class SourceServiceImpl implements SourceService{
    private final SourceRepository repository;

    public SourceServiceImpl(SourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Source> getSources() {
        return repository.findAll();
    }

    @Override
    public Map<Long, Source> getSourcesMap() {
        List<Source> sourceList = getSources();
        return sourceList.stream().collect(Collectors.toMap(Source::getSourceId, source -> source));
    }

    @Override
    public List<Source> getSpecificSourcesMap(List<Long> sourceIds) {
        List<Source> selectedSources = new ArrayList<>();
        Map<Long, Source> originSources = getSourcesMap();
        for (Long sourceId: sourceIds) {
            Source source = originSources.get(sourceId);
            selectedSources.add(source);
        }
        return selectedSources;
    }
}
