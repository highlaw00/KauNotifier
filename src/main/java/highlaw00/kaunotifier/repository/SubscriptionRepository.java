package highlaw00.kaunotifier.repository;

import highlaw00.kaunotifier.entity.Subscription;
import highlaw00.kaunotifier.entity.User;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    List<Subscription> saveAll(List<Subscription> subscriptions);
    List<Subscription> findAllWithUserId(Long id);
    List<Subscription> findAll();
    Optional<Subscription> deleteById(Long id);
}
