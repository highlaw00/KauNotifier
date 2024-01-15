package kauproject.kaunotifier.service;

import jakarta.transaction.Transactional;
import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.domain.Subscription;
import kauproject.kaunotifier.repository.MemberRepository;
import kauproject.kaunotifier.repository.SourceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SubscriptionServiceTest {

    @Autowired private SubscriptionService subscriptionService;
    @Autowired private SourceRepository sourceRepository;
    @Autowired private MemberService memberService;

    @Test
    void subscribe() {
        // given
        Member testA = Member.createMember("테스트 멤버", "test123@gmail.com");
        memberService.join(testA);
        List<Source> sourceList = sourceRepository.findAllList();
        int sourceSize = sourceList.size();

        List<Subscription> beforeSubscriptionList = subscriptionService.findSubscriptionOfMember(testA);
        int beforeSize = beforeSubscriptionList.size();

        // when
        subscriptionService.subscribe(testA, sourceList);
        List<Subscription> afterSubscriptionList = subscriptionService.findSubscriptionOfMember(testA);
        int afterSize = afterSubscriptionList.size();

        // then
        assertThat(afterSize).isEqualTo(beforeSize + sourceSize);

    }

}
