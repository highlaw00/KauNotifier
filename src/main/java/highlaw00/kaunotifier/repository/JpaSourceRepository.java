package highlaw00.kaunotifier.repository;

import highlaw00.kaunotifier.entity.Source;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
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
        /*
         * JPQL을 활용해 엔티티를 조회해도 영속 컨텍스트 내부에 존재하게 된다.
         * 즉, 아래 코드가 처음 실행되고 그 결과가 캐시에 저장되기 때문에 아래 코드가 계속해서 DB에 접근하는 일의 가능성은 낮음.
         */
        return em.createQuery("select s from Source s", Source.class)
                .getResultList();
    }
}
