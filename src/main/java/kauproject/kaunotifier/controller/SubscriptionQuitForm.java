package kauproject.kaunotifier.controller;

import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.domain.Subscription;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubscriptionQuitForm {
    private String email;
    private String name;
    private List<Subscription> subscriptionList;
}
