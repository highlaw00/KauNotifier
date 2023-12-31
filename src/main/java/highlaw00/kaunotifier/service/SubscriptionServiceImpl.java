package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.Source;
import highlaw00.kaunotifier.entity.Subscription;
import highlaw00.kaunotifier.entity.User;
import highlaw00.kaunotifier.repository.SourceRepository;
import highlaw00.kaunotifier.repository.SubscriptionRepository;
import highlaw00.kaunotifier.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Transactional // -> 메서드가 정상 종료된 경우 트랜잭션이 커밋됨. 예외가 발생하면 롤백시킴.
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * 최초 구독시 실행되는 메서드
     */
    @Override
    public List<Subscription> subscribe(User user, List<Source> sourceList) {
        // TODO: USER_ID VALIDATION
        List<Subscription> subscriptionList = new ArrayList<>();

        for (Source source: sourceList) {
            Subscription subscription = new Subscription();
            subscription.source = source;
            subscription.user = user;
            subscriptionList.add(subscription);
        }

        return subscriptionRepository.saveAll(subscriptionList);
    }

    @Override
    public Optional<Subscription> updateSubscription(User user, List<Source> sourceList) {
        return Optional.empty();
    }

    @Override
    public List<Subscription> getSubscriptions(User user) {
        return subscriptionRepository.findAllWithUserId(user.getUserId());
    }

//    private boolean isUserExists(User user) {
//        String email = user.getEmail();
//        return userRepository.findByEmail(email).isPresent();
//    }

    /*
     * 이메일 포맷이 부합하는 학번인지 확인
     */
    private void validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(regex, email)) {
            throw new IllegalArgumentException("옳지 못한 이메일입니다.");
        }
    }

}
