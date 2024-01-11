package kauproject.kaunotifier.repository;

import jakarta.persistence.EntityManager;
import kauproject.kaunotifier.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    void save(Member member) {
        em.persist(member);
    }
}
