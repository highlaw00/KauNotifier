package highlaw00.kaunotifier.controller;

import highlaw00.kaunotifier.entity.Source;
import highlaw00.kaunotifier.entity.Subscription;
import highlaw00.kaunotifier.entity.User;
import highlaw00.kaunotifier.service.SourceService;
import highlaw00.kaunotifier.service.SubscriptionService;
import highlaw00.kaunotifier.service.SubscriptionServiceImpl;
import highlaw00.kaunotifier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SubscribeController {
    private final SubscriptionService subscriptionService;
    private final SourceService sourceService;
    private final UserService userService;

    @Autowired
    public SubscribeController(SubscriptionService subscriptionService, SourceService sourceService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.sourceService = sourceService;
        this.userService = userService;
    }

    @GetMapping("/subscription/subscribe")
    public String getSubscribe(Model model) {
        List<Source> sourceList = sourceService.getSources();
        model.addAttribute(sourceList);
        return "/subscription/subscribe";
    }

    @PostMapping("/subscription/subscribe")
    public String postSubscribe(SubscriptionForm form, RedirectAttributes redirectAttributes) {
        List<Long> rawSources = form.getSourceList();
        List<Source> selectedSources = sourceService.getSpecificSourcesMap(rawSources);
        // TODO: Email validation
        String email = form.getEmail();
        User user = new User();
        user.setEmail(email);

        userService.join(user);
        subscriptionService.subscribe(user, selectedSources);

        redirectAttributes.addAttribute("email", email);
        return "redirect:/subscription/{email}";
    }

    @GetMapping("/subscription/{email}")
    public String getSubscription(@PathVariable String email, Model model) {
        Optional<User> userOptional = userService.findOneByEmail(email);
        boolean isValidUser = userOptional.isPresent();

        if (isValidUser) {
            User user = userOptional.get();
            List<Subscription> subscriptions = subscriptionService.getSubscriptions(user);
            model.addAttribute("status", true);
            model.addAttribute("user", user);
            model.addAttribute("subscriptions", subscriptions);
        } else {
            model.addAttribute("status", false);
        }

        return "/subscription/single";
    }

    @GetMapping("/subscriptions")
    public String showSubscriptions(Model model) {
//        List<Subscription> subscriptions = subscriptionServiceImpl.findAll();
//        model.addAttribute("subscriptions", subscriptions);

        return "/subscription/show";
    }

    @GetMapping("/subscription/find")
    public String findSubscription() {
        return "/subscription/find";
    }

    @PostMapping("/subscription/find")
    public String postConfig(ConfigForm form, RedirectAttributes redirectAttributes) {
        String email = form.getEmail();
//        subscriptionServiceImpl.findOne(email).ifPresentOrElse(
//            s -> {
//                redirectAttributes.addAttribute("status", true);
//                redirectAttributes.addAttribute("email", email);
//            },
//            () -> {
//                redirectAttributes.addAttribute("status", false);
//            });

        return "redirect:/subscription/{email}";
    }
}
