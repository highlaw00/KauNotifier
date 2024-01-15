package kauproject.kaunotifier.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kauproject.kaunotifier.domain.Source;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SourceRepository {

    private final EntityManager em;

    public List<Source> findAll() {
        return em.createQuery("select s from Source s", Source.class)
                .getResultList();
    }
}
