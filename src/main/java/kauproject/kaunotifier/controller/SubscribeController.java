package kauproject.kaunotifier.controller;

import jakarta.validation.Valid;
import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.domain.Subscription;
import kauproject.kaunotifier.redis.RedisService;
import kauproject.kaunotifier.repository.SourceRepository;
import kauproject.kaunotifier.service.MemberService;
import kauproject.kaunotifier.service.SubscriptionService;
import kauproject.kaunotifier.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SubscribeController {

    private final SubscriptionService subscriptionService;
    private final MemberService memberService;
    private final SourceRepository sourceRepository;
    private final VerificationService verificationService;

    /**
     * 문자열 trim 미들웨어
     */
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("subscriptions/subscribe")
    public String subscribeHome(Model model) {
        SubscriptionForm subscriptionForm = new SubscriptionForm();
        subscriptionForm.setSourceMap(sourceRepository.findAllMap());
        model.addAttribute("subscriptionForm", subscriptionForm);
        return "subscription/subscribe";
    }

    @PostMapping("subscriptions/subscribe")
    public String subscribe(@Validated @ModelAttribute SubscriptionForm subscriptionForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        String name = subscriptionForm.getName();
        String email = subscriptionForm.getEmail();
        Member member = Member.createMember(name, email);
        List<Source> selectedSources = getSelectedSources(subscriptionForm);

        // 중복 회원 검증 로직
        if (memberService.isDuplicatedMember(member)) {
            bindingResult.addError(new ObjectError("memberErr", "이미 등록된 이메일입니다."));
        }

        // Source Validation
        if (selectedSources.isEmpty()) {
            bindingResult.addError(new ObjectError("sourcesError", "공지사항 출처를 하나 이상 선택해주세요."));
        }

        // BindingResult 사용
        if (bindingResult.hasErrors()) {
            subscriptionForm.setSourceMap(sourceRepository.findAllMap());
            model.addAttribute("subscriptionForm", subscriptionForm);
            return "subscription/subscribe";
        }

        // 성공 로직 -> 저장을 일단하기
        memberService.join(member);
        subscriptionService.subscribe(member, selectedSources);
        redirectAttributes.addAttribute("email", email);
        redirectAttributes.addAttribute("name", name);
        redirectAttributes.addAttribute("status", true);

        return "redirect:/subscriptions/{email}";
    }

    @GetMapping("subscriptions/find")
    public String getFind(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "subscription/find";
    }

    @PostMapping("subscriptions/find")
    public String findMember(@Validated @ModelAttribute MemberForm memberForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        String name = memberForm.getName();
        String email = memberForm.getEmail();

        if (memberService.find(Member.createMember(name, email)).isEmpty()) {
            bindingResult.addError(new ObjectError("memberErr", "존재하지 않거나 이름과 이메일이 맞지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            // BindingResult는 자동으로 뷰에 넘어감!
            // redirect를 하는것이 아니다...
            return "subscription/find";
        }

        redirectAttributes.addAttribute("status", true);
        redirectAttributes.addAttribute("email", email);
        redirectAttributes.addAttribute("name", name);

        return "redirect:/subscriptions/{email}";
    }

    @GetMapping("subscriptions/{email}")
    public String showSingle(@PathVariable String email, @RequestParam String name, Model model) {
        Member member = Member.createMember(name, email);
        Optional<Member> memberOptional = memberService.find(member);
        if (memberOptional.isEmpty()) {
            return "redirect:/subscriptions/not-found";
        }

        Member findMember = memberOptional.get();
        log.info(String.valueOf(findMember));
        model.addAttribute("member", findMember);

        return "subscription/single";
    }

    // TODO: Refactor this.
    @GetMapping("subscriptions/{email}/edit")
    public String getEditSubscriptions(@PathVariable String email, @RequestParam String name, Model model) {
        Member member = Member.createMember(name, email);
        Optional<Member> memberOptional = memberService.find(member);
        if (memberOptional.isEmpty()) {
            return "redirect:/subscriptions/not-found";
        }

        List<Source> sourceList = sourceRepository.findAllList();

        SubscriptionForm subscriptionForm = new SubscriptionForm();
        subscriptionForm.setName(name);
        subscriptionForm.setEmail(email);
        subscriptionForm.setSources(sourceList);

        model.addAttribute("sources", sourceList);
        model.addAttribute("subscriptionForm", subscriptionForm);
        return "subscription/edit";
    }

    @PostMapping("subscriptions/{email}/edit")
    public String editSubscriptions(@PathVariable String email, @Validated @ModelAttribute SubscriptionForm subscriptionForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        String name = subscriptionForm.getName();
        Member member = Member.createMember(name, email);
        List<Source> selectedSources = getSelectedSources(subscriptionForm);

        // 존재 검증
        if (memberService.find(member).isEmpty()) {
            bindingResult.addError(new ObjectError("memberErr", "존재하지 않거나 이름과 이메일이 맞지 않습니다."));
        }

        // 출처 선택 검증
        if (selectedSources.isEmpty()) {
            bindingResult.addError(new ObjectError("sourcesError", "공지사항 출처를 하나 이상 선택해주세요."));
        }

        if (bindingResult.hasErrors()) {
            subscriptionForm.setSources(sourceRepository.findAllList());
            model.addAttribute("subscriptionForm", subscriptionForm);
            return "subscription/edit";
        }

        // 성공 로직
        member = memberService.find(member).get();
        subscriptionService.updateSubscribe(member, selectedSources);

        redirectAttributes.addAttribute("status", true);
        redirectAttributes.addAttribute("email", email);
        redirectAttributes.addAttribute("name", name);

        return "redirect:/subscriptions/{email}";
    }

    @GetMapping("subscriptions/{email}/quit")
    public String getQuit(@PathVariable String email, @RequestParam String name, Model model) {
        Member member = Member.createMember(name, email);
        Optional<Member> memberOptional = memberService.find(member);
        if (memberOptional.isEmpty()) {
            return "redirect:/subscriptions/not-found";
        }

        member = memberOptional.get();

        SubscriptionQuitForm subscriptionQuitForm = new SubscriptionQuitForm();
        subscriptionQuitForm.setName(name);
        subscriptionQuitForm.setEmail(email);
        System.out.println("subscriptionQuitForm.getName() = " + subscriptionQuitForm.getName());
        subscriptionQuitForm.setSubscriptionList(subscriptionService.findSubscriptionOfMember(member));

        model.addAttribute("subscriptionQuitForm", subscriptionQuitForm);
        return "subscription/quit";
    }

    @PostMapping("subscriptions/{email}/quit")
    public String quit(@PathVariable String email, @ModelAttribute SubscriptionQuitForm quitForm) {

        // TODO: Validate member
        Member member = memberService.find(Member.createMember(quitForm.getName(), email)).get();
        subscriptionService.unsubscribe(member);
        memberService.quit(member);

        return "redirect:/";
    }

    @GetMapping("subscriptions/not-found")
    public String notFound() {
        return "subscription/not-found";
    }

    @PostMapping("/subscribe")
    public SubscribeDto subscribe(@Valid @RequestBody SubscribeDto req, BindingResult result) {
        SubscribeDto res = new SubscribeDto();

        // 포맷 검증
        if (result.hasErrors()) {
            res.setMessage("이메일, 이름, 공지사항 체크 여부를 확인해주세요.");
            res.setResult("fail");
            return res;
        }

        // 이메일 인증 여부 검증
        if (!verificationService.verify(req.getEmail(), req.getCode())) {
            res.setMessage("인증 코드가 유효하지 않습니다.");
            res.setResult("fail");
            return res;
        }

        // 이메일 중복 검증
        String email = req.getEmail();
        String name = req.getName();
        Member member = Member.createMember(name, email);
        if (memberService.isDuplicatedMember(member)) {
            res.setMessage("이미 존재하는 회원입니다.");
            res.setResult("fail");
            return res;
        }

        // 회원 저장
        List<Source> selectedSources = getSelectedSourcesAlt(req.getSelectedSources());
        memberService.join(member);
        subscriptionService.subscribe(member, selectedSources);
        res.setEmail(email);
        res.setName(name);
        res.setResult("success");

        return res;
    }

    // TODO: Delete this method and usages.
    private List<Source> getSelectedSources(SubscriptionForm subscriptionForm) {
        List<Long> selectedSources = subscriptionForm.getSelectedSources();
        Map<Long, Source> sourceMap = sourceRepository.findAllMap();
        List<Source> selectedSourcesList = new ArrayList<>();

        for (Long sourceId : selectedSources) {
            if (sourceMap.containsKey(sourceId)) selectedSourcesList.add(sourceMap.get(sourceId));
        }

        return selectedSourcesList;
    }

    private List<Source> getSelectedSourcesAlt(List<Long> selectedIds) {
        Map<Long, Source> sourceMap = sourceRepository.findAllMap();
        List<Source> selectedSourcesList = new ArrayList<>();
        for (Long sourceId : selectedIds) {
            if (sourceMap.containsKey(sourceId)) selectedSourcesList.add(sourceMap.get(sourceId));
        }

        return selectedSourcesList;
    }

    private Member isNotExistingMember(String name, String email) {
        Member member = Member.createMember(name, email);
        Optional<Member> memberOptional = memberService.find(member);
        return memberOptional.orElse(member);
    }
}
