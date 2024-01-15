package kauproject.kaunotifier.controller;

import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.repository.SourceRepository;
import kauproject.kaunotifier.service.MemberService;
import kauproject.kaunotifier.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscriptionService subscriptionService;
    private final MemberService memberService;
    private final SourceRepository sourceRepository;

    @GetMapping("subscribe")
    public String subscribeHome(Model model) {
        List<Source> sourceList = sourceRepository.findAll();
        model.addAttribute("sources", sourceList);
        model.addAttribute("subscriptionForm", new SubscriptionForm());
        return "subscription/subscribe";
    }

    @PostMapping("subscribe")
    public String subscribe(@ModelAttribute SubscriptionForm subscriptionForm) {
        String name = subscriptionForm.getName();
        List<String> selectedSources = subscriptionForm.getSelectedSources();
        System.out.println("name = " + name);
        System.out.println("selectedSources = " + selectedSources);

        return "redirect:/";
    }
}
