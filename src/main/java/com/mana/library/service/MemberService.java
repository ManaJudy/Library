package com.mana.library.service;


import com.mana.library.entity.Loan;
import com.mana.library.entity.Member;
import com.mana.library.exceptionhandler.exception.EmailAlreadyUsedException;
import com.mana.library.exceptionhandler.exception.EntityNotFoundException;
import com.mana.library.exceptionhandler.exception.PenalityFoundException;
import com.mana.library.repository.LoanRepository;

import com.mana.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;


@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private PaymentService paymentService;

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member with ID " + id + " not found."));
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public List<Member> saveMembers(List<Member> members) {
        return memberRepository.saveAll(members);
    }

    public Member createMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail()))
            throw new EmailAlreadyUsedException("Email : " + member.getEmail() + " is already used.");
        return memberRepository.save(member);
    }

    public double calculatePenality(Member member) {
        double penalty = 0.0;
        List<Loan> loans = loanRepository.findAllByMemberInLoanAndReturnedFalse(member);
        LocalDate today = LocalDate.now();
        for (Loan loan : loans)
            if (loan.getReturnDate().isBefore(today)) {
                long daysOverdue = ChronoUnit.DAYS.between(loan.getReturnDate(), today);
                penalty += daysOverdue * 0.5;
            }
        return penalty;
    }

    public void payPenalty(Member member, double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be positive.");
        double currentPenalty = member.getPenaltyAmount();
        if (amount > currentPenalty) throw new IllegalArgumentException("Amount exceeds current penalty.");
        member.setPenaltyAmount(currentPenalty - amount);
        memberRepository.save(member);
    }

    public boolean shouldBanMember(Member member) {
        LocalDate today = LocalDate.now();
        List<Loan> loans = loanRepository.findAllByMemberInLoanAndReturnedFalse(member);
        return loans.stream().filter(loan -> loan.getReturnDate().isBefore(today))
                .anyMatch(loan -> ChronoUnit.DAYS.between(loan.getReturnDate(), today) > 30);
    }

    public void extendSubscription(Member member) {
        if (member.getPenaltyAmount() > 0)
            throw new PenalityFoundException("Member " + member.getName() + " has a penalty to pay before extending subscription.");
        LocalDate expirationDate = member.getExpirationDate();
        member.setExpirationDate(Objects.requireNonNullElseGet(expirationDate, LocalDate::now).plusYears(1));
        memberRepository.save(member);
    }

}
