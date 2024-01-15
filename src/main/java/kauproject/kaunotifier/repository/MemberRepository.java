package kauproject.kaunotifier.repository;

import jakarta.persistence.EntityManager;
import kauproject.kaunotifier.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public Optional<Member> findByNameAndEmail(String name, String email) {
        return Optional.ofNullable(em.createQuery("select m from Member m where m.name = :name and m.email = :email", Member.class)
                .setParameter("name", name)
                .setParameter("email", email)
                .getSingleResult());
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
