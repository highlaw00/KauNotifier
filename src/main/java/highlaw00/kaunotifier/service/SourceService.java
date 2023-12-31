package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.Source;

import java.util.List;
import java.util.Map;

public interface SourceService {
    List<Source> getSources();
    Map<Long, Source> getSourcesMap();
    List<Source> getSpecificSourcesMap(List<Long> sourceIds);
}
