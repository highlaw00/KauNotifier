package kauproject.kaunotifier.controller;

import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubscriptionForm {

    private String name;
    private String email;
    private List<Long> selectedSources = new ArrayList<>();
}
