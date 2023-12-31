package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.Source;
import highlaw00.kaunotifier.entity.Subscription;
import highlaw00.kaunotifier.entity.User;
import highlaw00.kaunotifier.repository.SubscriptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
public class SubscriptionServiceImplTest {
    private final SubscriptionService subscriptionService;
    private final SourceService sourceService;
    private final UserService userService;

    @Autowired
    public SubscriptionServiceImplTest(SubscriptionService subscriptionService, SourceService sourceService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.sourceService = sourceService;
        this.userService = userService;
    }

    @Test
    public void 구독하기() throws Exception {
        //given
        List<Source> sources = sourceService.getSources().subList(0, 3);
        User user = new User();
        user.setEmail("choiyool00@kau.kr");
        // persist했을 때 값이 알아서 증가한다? -> Generated Value 덕분인가?
        userService.join(user);

        //when
        List<Subscription> result = subscriptionService.subscribe(user, sources);

        //then
        List<Subscription> findSubscriptions = subscriptionService.getSubscriptions(user);
        Assertions.assertThat(result.size()).isEqualTo(findSubscriptions.size());
    }

    @Test
    public void 중복_구독_예외() throws Exception {
//        //given
//        Subscription subscription1 = new Subscription();
////        subscription1.setEmail("choiyool00@gmail.com");
//
//        Subscription subscription2 = new Subscription();
////        subscription2.setEmail("choiyool00@gmail.com");
//
//        //when
//        String savedEmail = subscriptionServiceImpl.join(subscription1);
//        IllegalArgumentException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
//                () -> subscriptionServiceImpl.join(subscription2));
//
//        //then
//        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }

    @Test
    public void 구독확인() throws Exception {
//        //given
//        Subscription subscription = new Subscription();
////        subscription.setEmail("choiyool00@gmail.com");
//
//        //when
//        String savedEmail = subscriptionServiceImpl.join(subscription);
//
//        //then
//        Subscription result = subscriptionServiceImpl.findOne(savedEmail).get();
//        Assertions.assertThat(subscription).isEqualTo(result);
    }
}
