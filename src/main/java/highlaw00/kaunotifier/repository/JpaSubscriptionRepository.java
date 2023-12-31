package highlaw00.kaunotifier.repository;

import highlaw00.kaunotifier.entity.Subscription;
import highlaw00.kaunotifier.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class JpaSubscriptionRepository implements SubscriptionRepository{

    private final EntityManager em;

    public JpaSubscriptionRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Subscription save(Subscription subscription) {
        em.persist(subscription);
        return subscription;
    }

    @Override
    public List<Subscription> saveAll(List<Subscription> subscriptions) {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        for (Subscription subscription : subscriptions) {
            em.persist(subscription);
        }

        transaction.commit();

        return subscriptions;
    }

    @Override
    public List<Subscription> findAllWithUserId(Long id) {
        return em.createQuery("select s from Subscription s where s.user = :id", Subscription.class)
                .getResultList();
    }

    @Override
    public List<Subscription> findAll() {
        return em.createQuery("select s from Subscription s", Subscription.class)
                .getResultList();
    }

    @Override
    public Optional<Subscription> deleteById(Long id) {
        Subscription targetSubscription = em.find(Subscription.class, id);
        em.remove(targetSubscription);
        return Optional.ofNullable(targetSubscription);
    }
}
