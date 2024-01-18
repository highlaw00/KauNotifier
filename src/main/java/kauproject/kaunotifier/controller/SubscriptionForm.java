package kauproject.kaunotifier.controller;

import jakarta.validation.constraints.*;
import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubscriptionForm {

    @NotBlank
    @Size(min=2, max=10, message = "이름은 최소 2글자에서 10글자여야 합니다.")
    private String name;

    @NotBlank
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    private List<Source> sources;
    private List<Long> selectedSources;
}
