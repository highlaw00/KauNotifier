package kauproject.kaunotifier.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kauproject.kaunotifier.domain.Source;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SourceRepository {

    private final EntityManager em;

    public List<Source> findAllList() {
        return em.createQuery("select s from Source s", Source.class)
                .getResultList();
    }

    public Map<Long, Source> findAllMap() {
        return em.createQuery("select s from Source s", Source.class)
                .getResultStream()
                .collect(
                        Collectors.toMap(
                                s -> (s.getId()),
                                s -> (s)
                        )
                );
    }
}
