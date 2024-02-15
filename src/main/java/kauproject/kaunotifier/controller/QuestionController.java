package kauproject.kaunotifier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionController {
    @GetMapping("/questions")
    public String getQuestions() {
        return "questions";
    }
}
