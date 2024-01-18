package kauproject.kaunotifier.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}
