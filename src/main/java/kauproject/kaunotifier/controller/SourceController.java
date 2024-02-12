package kauproject.kaunotifier.controller;
import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SourceController {
    private final SourceRepository sourceRepository;

    @GetMapping("/api/sources")
    public SourceDto getSources() {
        Map<Long, Source> sources = sourceRepository.findAllMap();
        SourceDto sourceDto = new SourceDto();
        sourceDto.setSourceMap(sources);

        return sourceDto;
    }
}
