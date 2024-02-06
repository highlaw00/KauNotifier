package kauproject.kaunotifier.controller;

import jakarta.validation.Valid;
import kauproject.kaunotifier.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailVerificationController {

    private final RedisService redisService;

    @PostMapping("/verify-request")
    public VerificationDto sendEmail(@Valid @RequestBody VerificationDto req, BindingResult result) {

        VerificationDto res = new VerificationDto();

        // 이메일이 올바른 형식이 아닌 경우
        if (result.hasErrors()) {
            res.setMessage("올바른 이메일 형식이 아닙니다.");
            res.setResult("fail");
            return res;
        }

        String email = req.getEmail();

        // 최근 이메일 전송을 시도한 경우
        if (!redisService.getValues(email).equals("false")) {
            res.setMessage("이미 이메일 인증 코드를 발송하였습니다. 계속해서 발생하는 경우 개발자에게 문의하세요.");
            res.setResult("fail");
            return res;
        }

        // 성공 로직
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);
        String sixDigitRandomNumber = String.format("%06d", randomNumber);

        // 캐시에 저장 및 이메일 발송
        redisService.setValues(email, sixDigitRandomNumber, Duration.ofMinutes(3L));

        res.setEmail(email);
        res.setResult("success");

        return res;
    }

    @PostMapping("/verify")
    public VerificationDto verify(@Valid @RequestBody VerificationDto req) {
        String email = req.getEmail();
        String code = req.getCode();
        String findValue = redisService.getValues(email);
        if (!Objects.equals(findValue, code)) {
            req.setResult("fail");
            req.setMessage("올바른 인증 코드가 아닙니다.");
        } else {
            req.setResult("success");
        }

        return req;
    }

//    /**
//     * Verification code 발송
//     */
//    @PostMapping("/verify")
//    public String sendVerificationCode(@Validated @ModelAttribute SubscriptionForm subscriptionForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
//
//        String email = subscriptionForm.getEmail();
//        List<Long> selectedSources = subscriptionForm.getSelectedSources();
//
//        System.out.println("selectedSources = " + selectedSources);
//
//         Random 객체 생성
//        Random random = new Random();
//
//        // 0부터 999999 사이의 랜덤한 정수 생성
//        int randomNumber = random.nextInt(1000000);
//
//        // 생성된 랜덤 숫자를 6자리로 맞추기 위해 문자열로 변환 후 출력
//        String sixDigitRandomNumber = String.format("%06d", randomNumber);
//
//        redisService.setValues(email, sixDigitRandomNumber, Duration.ofMinutes(3L));
//
//        redirectAttributes.addAttribute("email", email);
//        redirectAttributes.addAttribute("name", subscriptionForm.getName());
//        redirectAttributes.addAttribute("selectedSources", selectedSources);
//
//        return "redirect:/verify/{email}";
//    }
//
//    /**
//     * 검증 코드 입력 폼 반환
//     */
//    @GetMapping("/verify/{email}")
//    public String getVerifyPage(@PathVariable(name = "email") String email, @RequestParam(name = "name") String name, Model model) {
//        SubscriptionForm subscriptionForm = new SubscriptionForm();
//        subscriptionForm.setEmail(email);
//        subscriptionForm.setName(name);
//        model.addAttribute("subscriptionForm", subscriptionForm);
//
//        return "verification/verify";
//    }
//
//    /**
//     * Verification code 검증
//     */
//    @PostMapping("/verify/{email}")
//    public String verify(@PathVariable(name = "email") String email, @ModelAttribute SubscriptionForm subscriptionForm) {
//        System.out.println("subscriptionForm = " + subscriptionForm.getEmail());
//        System.out.println("subscriptionForm = " + subscriptionForm.getName());
//        System.out.println("subscriptionForm = " + subscriptionForm.getCode());
//        System.out.println("subscriptionForm = " + subscriptionForm.getSelectedSources());
//
//        return "index";
//    }
}
