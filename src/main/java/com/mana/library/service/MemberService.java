package com.mana.library.service;

import com.mana.library.entity.Member;
import com.mana.library.exeptionhandler.exeption.EmailAlreadyUsedException;
import com.mana.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;


    public Member saveMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail()))
            throw new EmailAlreadyUsedException("Email : " + member.getEmail() + " is already used.");
        LocalDate expirationDate = LocalDate.now().plusYears(1);
        member.setExpirationDate(expirationDate);
        return memberRepository.save(member);
    }
}
