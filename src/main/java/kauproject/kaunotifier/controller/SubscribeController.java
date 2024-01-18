package kauproject.kaunotifier.controller;

import jakarta.validation.Valid;
import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.domain.Subscription;
import kauproject.kaunotifier.repository.SourceRepository;
import kauproject.kaunotifier.service.MemberService;
import kauproject.kaunotifier.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SubscribeController {

    private final SubscriptionService subscriptionService;
    private final MemberService memberService;
    private final SourceRepository sourceRepository;

    @GetMapping("subscriptions/subscribe")
    public String subscribeHome(Model model) {
        SubscriptionForm subscriptionForm = new SubscriptionForm();
        subscriptionForm.setSources(sourceRepository.findAllList());
        model.addAttribute("subscriptionForm", subscriptionForm);
        return "subscription/subscribe";
    }

    @PostMapping("subscriptions/subscribe")
    public String subscribe(@Valid SubscriptionForm subscriptionForm, BindingResult bindingResult, Model model) {

        String name = subscriptionForm.getName();
        String email = subscriptionForm.getEmail();
        Member member = Member.createMember(name, email);
        List<Source> selectedSources = getSelectedSources(subscriptionForm);

        // BindingResult 사용
        if (bindingResult.hasErrors()) {
            subscriptionForm.setSources(sourceRepository.findAllList());
            model.addAttribute("subscriptionForm", subscriptionForm);
            return "subscription/subscribe";
        }

        // 성공 로직
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
    public String findMember(@Validated @ModelAttribute MemberForm memberForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // BindingResult는 자동으로 뷰에 넘어감!
            // redirect를 하는것이 아니다...
            return "/subscription/find";
        }

        String name = memberForm.getName();
        String email = memberForm.getEmail();

        Member findMember = isNotExistingMember(name, email);

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
        Member findMember = isNotExistingMember(name, email);

        // 찾은 회원이 존재하지 않음
        if (findMember.getId() == null) {
            return "redirect:/subscriptions/not-found";
        }

        model.addAttribute("member", findMember);

        return "/subscription/single";
    }

    @GetMapping("/subscriptions/{email}/edit")
    public String getEditSubscriptions(@PathVariable String email, @RequestParam String name, Model model) {
        Member findMember = isNotExistingMember(name, email);

        // 찾은 회원이 존재하지 않음
        if (findMember.getId() == null) {
            return "redirect:/subscriptions/not-found";
        }

        Map<Long, Source> subscribedSourceMap = findMember.getSubscriptions()
                .stream()
                .collect(
                        Collectors.toMap(
                                subscription -> (subscription.getSource().getId()),
                                Subscription::getSource
                        )
                );

        List<Source> sourceList = sourceRepository.findAllList();
        model.addAttribute("sources", sourceList);
        model.addAttribute("member", findMember);
        model.addAttribute("subscribedSourceMap", subscribedSourceMap);
        model.addAttribute("subscriptionForm", new SubscriptionForm());
        return "/subscription/edit";
    }

    @PostMapping("/subscriptions/{email}/edit")
    public String editSubscriptions(@PathVariable String email, @ModelAttribute SubscriptionForm subscriptionForm, RedirectAttributes redirectAttributes) {
        String formName = subscriptionForm.getName();
        Member findMember = isNotExistingMember(formName, email);

        if (findMember.getId() == null) {
            return "redirect:/subscriptions/not-found";
        }

        List<Source> selectedSources = getSelectedSources(subscriptionForm);
        for (Source selectedSource : selectedSources) {
            System.out.println("selectedSource = " + selectedSource.getDescription());
        }

        subscriptionService.updateSubscribe(findMember, selectedSources);

        redirectAttributes.addAttribute("status", true);
        redirectAttributes.addAttribute("email", email);
        redirectAttributes.addAttribute("name", formName);

        return "redirect:/subscriptions/{email}";
    }

    @GetMapping("/subscriptions/{email}/quit")
    public String getQuit(@PathVariable String email, @RequestParam String name, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("subscriptionQuitForm", new SubscriptionQuitForm());
        return "/subscription/quit";
    }

    @PostMapping("/subscriptions/{email}/quit")
    public String quit(@PathVariable String email, @RequestParam String name, @ModelAttribute SubscriptionQuitForm quitForm, Model model) {
        // TODO: Validate member
        Member member = memberService.find(Member.createMember(name, email)).get();
        subscriptionService.unsubscribe(member);
        memberService.quit(member);

        return "redirect:/";
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

    private Member isNotExistingMember(String name, String email) {
        Member member = Member.createMember(name, email);
        Optional<Member> memberOptional = memberService.find(member);
        return memberOptional.orElse(member);
    }
}
