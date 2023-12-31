package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.Subscription;
import highlaw00.kaunotifier.repository.SubscriptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class SubscriptionServiceImplTest {
    SubscriptionServiceImpl subscriptionServiceImpl;
    SubscriptionRepository subscriptionRepository;

    @Test
    public void 구독하기() throws Exception {
        //given
        //TODO: 서비스 테스트
//        subscription.setEmail("choiyool00@gmail.com");

        //when
//        String savedEmail = subscriptionServiceImpl.join(subscription);

        //then
//        Subscription result = subscriptionRepository.findByEmail(savedEmail).get();
//        Assertions.assertThat(subscription).isEqualTo(result);
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
