package kauproject.kaunotifier.service;

import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.domain.Subscription;
import kauproject.kaunotifier.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public void subscribe(Member member, List<Source> sourceList) {
        List<Subscription> subscriptionList = new ArrayList<>();
        for (Source source : sourceList) {
            Subscription subscription = Subscription.createSubscription(member, source);
            subscriptionList.add(subscription);
        }

        subscriptionRepository.saveMany(subscriptionList);
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public List<Subscription> findSubscriptionOfMember(Member member) {
        return subscriptionRepository.findAllByMember(member);
    }
}
