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

    @Scheduled(cron = "0 0 0 * * ?")
    public void performDailyUpdate() {
        List<Member> members = memberService.findMembers();
        members.forEach(member -> {
            double penality = memberService.calculatePenality(member);
            member.setPenaltyAmount(penality);
            member.setActive(memberService.shouldBanMember(member));
        });
        memberService.saveMembers(members);
    }
}
