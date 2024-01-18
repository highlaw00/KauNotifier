package kauproject.kaunotifier.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    @NotBlank
    @Size(min=2, max=10, message = "이름은 최소 2글자에서 10글자여야 합니다.")
    private String name;

    @NotBlank
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
}
