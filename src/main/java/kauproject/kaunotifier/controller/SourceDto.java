package kauproject.kaunotifier.controller;

import kauproject.kaunotifier.domain.Source;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SourceDto {
    private Map<Long, Source> sourceMap;
}
