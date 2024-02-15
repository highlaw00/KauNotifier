package kauproject.kaunotifier.repository;

import jakarta.persistence.EntityManager;
import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepository {

    private final EntityManager em;

    public Long save(Subscription subscription) {
        em.persist(subscription);
        return subscription.getId();
    }

    public List<Subscription> saveMany(List<Subscription> subscriptionList) {
        for (Subscription subscription : subscriptionList) {
            em.persist(subscription);
        }
        return subscriptionList;
    }

    public Optional<Subscription> findOne(Long memberId, Long sourceId) {
        List<Subscription> result = em.createQuery("select s from Subscription s where s.member.id = :memberId and s.source.id = :sourceId", Subscription.class)
                .setParameter("memberId", memberId)
                .setParameter("sourceId", sourceId)
                .getResultList();
        return result.stream().findAny();
    }

    public Optional<Subscription> findById(Long id) {
        return Optional.ofNullable(em.find(Subscription.class, id));
    }

    public List<Subscription> findAll() {
        return em.createQuery("select s from Subscription s", Subscription.class).getResultList();
    }

    /**
     * 사용자 ID를 기준으로 구독 탐색
     * @return
     */
    public List<Subscription> findAllByMember(Member member) {
        return em.createQuery("select s from Subscription s where s.member.id = :id", Subscription.class)
                .setParameter("id", member.getId())
                .getResultList();
    }

    public void deleteOne(Subscription subscription) {
        em.remove(subscription);
    }

}
