package kauproject.kaunotifier.repository;

import kauproject.kaunotifier.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;

    @Test
    void saveTest() {
        // given
        Member member = Member.createMember("test", "test@gmail.com");

        // when
        Long findId = memberRepository.save(member);

        // then
        assertThat(findId).isEqualTo(member.getId());
    }

    @Test
    void findTest() {
        // given
        Member memberA = Member.createMember("testA", "testA@gmail.com");
        Member memberB = Member.createMember("testB", "testB@gmail.com");
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        Long targetId = memberA.getId();

        String targetName = memberB.getName();
        String targetEmail = memberB.getEmail();


        // then
        memberRepository.findById(targetId).ifPresentOrElse(
                member -> { assertThat(member.getId()).isEqualTo(targetId); },
                () -> { fail("Target Not Found: findById"); }
        );
        memberRepository.findByNameAndEmail(targetName, targetEmail).ifPresentOrElse(
                member -> { assertThat(member.getEmail()).isEqualTo(targetEmail); },
                () -> { fail("Target Not Found: findByEmail"); }
        );
    }

    @Test
    void findAll() {
        // given
        int beforeSize = memberRepository.findAll().size();

        Member memberA = Member.createMember("testA", "testA@gmail.com");
        Member memberB = Member.createMember("testB", "testB@gmail.com");
        int count = 2;

        // when
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        int afterSize = memberRepository.findAll().size();

        // then
        assertThat(afterSize).isEqualTo(beforeSize + 2);
    }
}
