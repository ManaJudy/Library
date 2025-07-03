package com.mana.library.controller;

import com.mana.library.entity.Member;
import com.mana.library.service.BrevoEmailService;
import com.mana.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private BrevoEmailService brevoEmailService;

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member savedMember = memberService.saveMember(member);
        brevoEmailService.sendMail(savedMember, "Library Membership Confirmation",
                "Dear " + savedMember.getName() + ",\n\nThank you for registering with our library. Your membership is now active.\n\nBest regards,\nLibrary Team");
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }
}
