package kauproject.kaunotifier.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubscriptionForm {

    @NotNull(message = "이름이 포함되어야 합니다.")
    @Size(min=2, max=10, message = "이름은 최소 2글자에서 10글자여야 합니다.")
    private String name;

    @NotNull(message = "이메일이 포함되어야 합니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    private List<Source> sources;
    private List<Long> selectedSources;
}
