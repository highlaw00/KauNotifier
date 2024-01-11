package kauproject.kaunotifier.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import kauproject.kaunotifier.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.fail;

@Transactional
class H2Connection {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("kaunotifier_h2");
    private final EntityManager em = emf.createEntityManager();

    @Test
    @Rollback(value = false)
    void 엔티티_테스트_h2() {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Member member = new Member();
            member.setName("hi");
            member.setEmail("hello");

            em.persist(member);
            tx.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail("트랜잭션 중 오류 발생");
        } finally {
            em.close();
        }

    }
}