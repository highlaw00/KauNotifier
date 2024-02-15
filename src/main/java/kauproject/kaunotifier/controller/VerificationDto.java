package kauproject.kaunotifier.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationDto {
    @NotBlank
    @Email
    private String email;
    private boolean isCertificated;
    private String Message;
    private String result;
    private String code;
}
