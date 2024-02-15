package kauproject.kaunotifier.service;

import kauproject.kaunotifier.domain.Member;
import kauproject.kaunotifier.domain.Source;
import kauproject.kaunotifier.domain.Subscription;
import kauproject.kaunotifier.repository.MemberRepository;
import kauproject.kaunotifier.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;

    public void subscribe(Member member, List<Source> sourceList) {
        List<Subscription> subscriptionList = new ArrayList<>();
        for (Source source : sourceList) {
            Subscription subscription = Subscription.createSubscription(member, source);
            subscription.getMember().addSubscription(subscription);
            subscriptionList.add(subscription);
        }

        subscriptionRepository.saveMany(subscriptionList);
    }

    public void updateSubscribe(Member member, List<Source> alteredSources) {
        Map<Long, Source> originSources = member.getSubscriptions().stream().collect(Collectors.toMap(sub -> (sub.getSource().getId()), Subscription::getSource));
        Map<Long, Source> alteredSourcesMap = alteredSources.stream().collect(Collectors.toMap(Source::getId, source -> source));

        // 새 구독에 포함되지 않는 기존 구독 지우기
        for (Long sourceId : originSources.keySet()) {
            if (alteredSourcesMap.containsKey(sourceId)) continue;
            Optional<Subscription> subscriptionOptional = subscriptionRepository.findOne(member.getId(), sourceId);
            subscriptionOptional.ifPresent(subscriptionRepository::deleteOne);
        }

        // 새 구독 추가하기
        for (Long sourceId : alteredSourcesMap.keySet()) {
            if (originSources.containsKey(sourceId)) continue;
            subscriptionRepository.save(Subscription.createSubscription(member, alteredSourcesMap.get(sourceId)));
        }

        // 기존 구독 바꾸기
        List<Subscription> alteredSourceList = alteredSources.stream().map(s -> Subscription.createSubscription(member, s)).toList();
        member.replaceSubscriptions(alteredSourceList);

    }

    public void unsubscribe(Member member) {
        for (Subscription subscription : member.getSubscriptions()) {
            subscriptionRepository.deleteOne(subscription);
        }
    }

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public List<Subscription> findSubscriptionOfMember(Member member) {
        return subscriptionRepository.findAllByMember(member);
    }
}
