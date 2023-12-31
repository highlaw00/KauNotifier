package highlaw00.kaunotifier.repository;

import highlaw00.kaunotifier.entity.Source;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaSourceRepository implements SourceRepository{
    private final EntityManager em;
    public JpaSourceRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public Source save(Source source) {
        em.persist(source);
        return source;
    }

    @Override
    public Optional<Source> findById(Long id) {
        Source source = em.find(Source.class, id);
        return Optional.ofNullable(source);
    }

    @Override
    public List<Source> findAll() {
        return em.createQuery("select s from Source s", Source.class)
                .getResultList();
    }
}
