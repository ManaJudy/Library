package com.mana.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mana.library.entity.Member;
import com.mana.library.service.BrevoEmailService;
import com.mana.library.service.MemberService;
import com.mana.library.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private BrevoEmailService brevoEmailService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Member> createMember(@RequestBody Map<String, Object> data) {
        // Extraction manuelle du membre
        Object memberObj = data.get("member");
        Object wayOfPaymentObj = data.get("wayOfPayment");
        Object amountObj = data.get("amount");

        if (memberObj == null || wayOfPaymentObj == null || amountObj == null) {
            return ResponseEntity.badRequest().build();
        }

        // Désérialiser explicitement "member" en Member
        Member member = new ObjectMapper().convertValue(memberObj, Member.class);
        String wayOfPayment = wayOfPaymentObj.toString();
        Double amount = Double.valueOf(amountObj.toString());

        Member savedMember = memberService.createMember(member);
        memberService.extendSubscription(savedMember);
        paymentService.createPayment(savedMember, wayOfPayment, amount);

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

//    @PostMapping("/{id}/pay-penality")
//    public ResponseEntity<String> payPenalty(@PathVariable Long id, @RequestParam double amount) {
//        Member member = memberService.findMemberById(id);
//        memberService.payPenalty(member, amount);
//        return new ResponseEntity<>("Penality paid", HttpStatus.CREATED);
//    }

    @PostMapping("/{id}/extend-subscription")
    public ResponseEntity<String> extendSubscription(@PathVariable Long id, @RequestParam String wayOfPayment, @RequestParam Double amount) {
        Member member = memberService.findMemberById(id);
        memberService.extendSubscription(member);
        paymentService.createPayment(member, wayOfPayment, amount);
        brevoEmailService.sendMail(member, "Library Extension Confirmation",
                "Dear " + member.getName() + ",\n\nYour library subscription has been successfully extended.\n\nBest regards,\nLibrary Team");
        return new ResponseEntity<>("Subscription extended and payment recorded", HttpStatus.CREATED);
    }

}
