package kauproject.kaunotifier.service;

import jakarta.persistence.EntityManager;
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
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SubscriptionServiceTest {

    @Autowired private SubscriptionService subscriptionService;
    @Autowired private SourceRepository sourceRepository;
    @Autowired private MemberService memberService;
    @Autowired private EntityManager em;

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

    @Test
    @Modifying(clearAutomatically = true)
    void updateSubscriptions() {
        // given
        Member member1 = Member.createMember("테스트 멤버", "test123@gmail.com");
        memberService.join(member1);
        List<Source> sourceList = sourceRepository.findAllList();
        subscriptionService.subscribe(member1, sourceList.subList(0, 3));

        em.flush();
        em.clear();

        // when
        int expectedSize = 3;
        Member member2 = memberService.find(Member.createMember("테스트 멤버", "test123@gmail.com")).get();

        
        subscriptionService.updateSubscribe(member2, sourceList.subList(1, 4));


        // then
        Assertions.assertThat(subscriptionService.findSubscriptionOfMember(member2).size()).isEqualTo(expectedSize);
        Assertions.assertThat(member2.getSubscriptions().size()).isEqualTo(expectedSize);
        Assertions.assertThat(member2.getSubscriptions().getLast().getSource()).isEqualTo(sourceList.get(3));
    }

}
