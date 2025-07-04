package com.mana.library.service;

import com.mana.library.entity.Subscription;
import com.mana.library.entity.Member;
import com.mana.library.repository.SubscriptionRepository;
import com.mana.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Subscription extendSubscription(Long memberId, Subscription.SubscriptionType type, Double amount) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));

        Subscription subscription = new Subscription(member, type, amount);
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getMemberSubscriptions(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Membre non trouvé"));

        return subscriptionRepository.findByMemberOrderByEndDateDesc(member);
    }

    public void processExpiredSubscriptions() {
        List<Subscription> expiredSubscriptions = subscriptionRepository
            .findExpiredSubscriptions(LocalDateTime.now());

        for (Subscription subscription : expiredSubscriptions) {
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
        }
    }
}
