package com.mana.library.controller;

import com.mana.library.entity.Member;
import com.mana.library.service.BrevoEmailService;
import com.mana.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private BrevoEmailService brevoEmailService;

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member savedMember = memberService.createMember(member);
        brevoEmailService.sendMail(savedMember, "Library Membership Confirmation",
                "Dear " + savedMember.getName() + ",\n\nThank you for registering with our library. Your membership is now active.\n\nBest regards,\nLibrary Team");
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        Member member = memberService.findMemberById(id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PostMapping("/{id}/pay-penality")
    public ResponseEntity<String> payPenalty(@PathVariable Long id, @RequestParam double amount) {
        Member member = memberService.findMemberById(id);
        memberService.payPenalty(member, amount);
        return new ResponseEntity<>("Penality paid", HttpStatus.CREATED);
    }
}
