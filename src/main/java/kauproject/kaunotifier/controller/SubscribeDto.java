package kauproject.kaunotifier.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubscribeDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
    private String code;
    private String result;
    private String message;
    @NotEmpty
    private List<Long> selectedSources;
}
