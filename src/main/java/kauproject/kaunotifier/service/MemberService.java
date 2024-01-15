package kauproject.kaunotifier.service;

import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(Member member) {
        memberRepository.save(member);
    }

}
