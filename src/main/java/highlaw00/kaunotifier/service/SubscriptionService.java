package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.Source;
import highlaw00.kaunotifier.entity.Subscription;
import highlaw00.kaunotifier.entity.User;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    List<Subscription> subscribe(User user, List<Source> sourceList);
    Optional<Subscription> updateSubscription(User user, List<Source> sourceList);
    List<Subscription> getSubscriptions(User user);
}
