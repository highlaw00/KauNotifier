package kauproject.kaunotifier.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.service.SubscriptionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
public class SubscriptionRepositoryTest {

    @Autowired EntityManager em;
    @Autowired SubscriptionRepository subscriptionRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired SourceRepository sourceRepository;
    @Autowired SubscriptionService subscriptionService;

    @Test
    void unsubscribe() {
        // given
        int beforeSize = subscriptionService.findAll().size();
        Member member = Member.createMember("test", "test@gmail.com");
        List<Source> sourceList = sourceRepository.findAllList();
        memberRepository.save(member);
        subscriptionService.subscribe(member, sourceList.subList(0, 3));
        em.flush();
        em.clear();

        // when
        Member member1 = memberRepository.findByNameAndEmail(member.getName(), member.getEmail()).get();
        subscriptionService.unsubscribe(member1);
        memberRepository.remove(member1);

        // then
        int afterSize = subscriptionService.findAll().size();
        Assertions.assertThat(afterSize).isEqualTo(beforeSize);
    }
}
