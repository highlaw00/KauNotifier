package kauproject.kaunotifier.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import kauproject.kaunotifier.domain.Source;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

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
    private List<Long> selectedSources;
    private List<Source> subscribingSources;
}
