package com.mana.library.service;

import com.mana.library.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyTaskService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ReservationService reservationService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void performDailyUpdate() {
        List<Member> members = memberService.findMembers();
        members.forEach(member -> {
            double penality = memberService.calculatePenality(member);
            member.setPenaltyAmount(penality);
            member.setActive(memberService.shouldBanMember(member));
        });
        memberService.saveMembers(members);

        // Traitement des réservations expirées
        reservationService.processExpiredReservations();
    }

    @Scheduled(cron = "0 0 9 * * ?") // Tous les jours à 9h
    public void checkAvailableReservations() {
        // Vérifier les réservations qui peuvent être notifiées
        reservationService.notifyAvailableReservations();
    }
}
