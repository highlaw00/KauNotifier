package highlaw00.kaunotifier.controller;

import highlaw00.kaunotifier.entity.Source;
import highlaw00.kaunotifier.entity.Subscription;
import highlaw00.kaunotifier.entity.User;
import highlaw00.kaunotifier.service.SubscriptionService;
import highlaw00.kaunotifier.service.SubscriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SubscribeController {
    private final SubscriptionService subscriptionServiceImpl;

    @Autowired
    public SubscribeController(SubscriptionServiceImpl subscriptionServiceImpl) {
        this.subscriptionServiceImpl = subscriptionServiceImpl;
    }

    @GetMapping("/subscription/subscribe")
    public String getSubscribe() {
        return "/subscription/subscribe";
    }

    @PostMapping("/subscription/subscribe")
    public String postSubscribe(SubscriptionForm form, RedirectAttributes redirectAttributes) {
        String email = form.getEmail();
        User user = new User();
        user.setEmail(email);
        subscriptionServiceImpl.subscribe(user, new ArrayList<>());
        redirectAttributes.addAttribute("email", email);
        // TODO: 실패시 실패 리다이렉션. 성공 시 구독 정보 조회 화면 리다이렉션
        return "redirect:/subscription/{email}";
    }

    @GetMapping("/subscription/{email}")
    public String getSubscription(@PathVariable String email, Model model) {
        // DB에서 이메일 조회. 있으면 그대로 보여주기, 없으면 안 보여줌.
//        subscriptionServiceImpl.findOne(email).ifPresentOrElse(
//                s -> {
//                    model.addAttribute("status", true);
//                    model.addAttribute("subscription", s);
//                },
//                () -> {
//                    model.addAttribute("status", false);
//                }
//        );

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
