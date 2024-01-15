package kauproject.kaunotifier.service;

import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(Member member) {
        memberRepository.save(member);
    }

    public void quit(Member member) {
        memberRepository.remove(member);
    }

    public Optional<Member> find(Member member) {
        return memberRepository.findByNameAndEmail(member.getName(), member.getEmail());
    }

}
