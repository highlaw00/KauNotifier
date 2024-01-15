package kauproject.kaunotifier.controller;

import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.repository.SourceRepository;
import kauproject.kaunotifier.service.MemberService;
import kauproject.kaunotifier.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscriptionService subscriptionService;
    private final MemberService memberService;
    private final SourceRepository sourceRepository;

    @GetMapping("subscribe")
    public String subscribeHome(Model model) {
        List<Source> sourceList = sourceRepository.findAllList();
        model.addAttribute("sources", sourceList);
        model.addAttribute("subscriptionForm", new SubscriptionForm());
        return "subscription/subscribe";
    }

    @PostMapping("subscribe")
    public String subscribe(@ModelAttribute SubscriptionForm subscriptionForm) {
        String name = subscriptionForm.getName();
        String email = subscriptionForm.getEmail();
        Member member = Member.createMember(name, email);
        List<Source> selectedSources = getSelectedSources(subscriptionForm);

        memberService.join(member);
        subscriptionService.subscribe(member, selectedSources);

        return "redirect:/";
    }

    @GetMapping("/subscriptions/find")
    public String getFind(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "subscription/find";
    }

    @PostMapping("/subscriptions/find")
    public String findMember(@ModelAttribute MemberForm memberForm, RedirectAttributes redirectAttributes) {
        String name = memberForm.getName();
        String email = memberForm.getEmail();

        Member member = Member.createMember(name, email);
        Optional<Member> memberOptional = memberService.find(member);
        Member findMember = memberOptional.orElseGet(() -> member);

        // 찾은 회원이 존재하지 않음
        if (findMember.getId() == null) {
            return "redirect:/subscriptions/not-found";
        }

        redirectAttributes.addAttribute("status", true);
        redirectAttributes.addAttribute("email", email);
        redirectAttributes.addAttribute("name", name);

        return "redirect:/subscriptions/{email}";
    }

    @GetMapping("/subscriptions/{email}")
    public String showSingle(@PathVariable String email, @RequestParam String name, Model model) {
        Member member = Member.createMember(name, email);
        Optional<Member> memberOptional = memberService.find(member);
        Member findMember = memberOptional.orElseGet(() -> member);

        model.addAttribute("member", findMember);

        return "/subscription/single";
    }

    private List<Source> getSelectedSources(SubscriptionForm subscriptionForm) {
        List<Long> selectedSources = subscriptionForm.getSelectedSources();
        Map<Long, Source> sourceMap = sourceRepository.findAllMap();
        List<Source> selectedSourcesList = new ArrayList<>();

        for (Long sourceId : selectedSources) {
            Source source = sourceMap.get(sourceId);
            selectedSourcesList.add(source);
        }

        return selectedSourcesList;
    }
}
