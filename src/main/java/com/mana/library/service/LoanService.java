package com.mana.library.service;

import com.mana.library.entity.*;
import com.mana.library.exceptionhandler.exception.EntityNotFoundException;
import com.mana.library.exceptionhandler.exception.PenaltyException;
import com.mana.library.exceptionhandler.exception.AccountExpiredException;
import com.mana.library.repository.CopyRepository;
import com.mana.library.repository.LoanRepository;
import com.mana.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CopyRepository copyRepository;

    public Loan createLoan(Member member, Book book, LoanType loanType) {
        if (member.isPenality()) throw new PenaltyException("Member has outstanding penalties.");
        if (!member.isActive()) throw new AccountExpiredException("Member account is expired.");
        Copy copy = copyRepository.findFirstCopyNotLoaned(book).orElseThrow(() -> new EntityNotFoundException("No available copies for book: " + book.getTitle()));
        Loan loan = new Loan();
        loan.setMemberInLoan(member);
        loan.setCopy(copy);
        loan.setLoanType(loanType);
        loan.setReturned(false);
        return loanRepository.save(loan);
    }

    public Loan extendLoan(Loan loan) {
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(LocalDate.now().plusDays(loan.getLoanType().getMaxLoanDays()));
        return loanRepository.save(loan);
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loan with ID " + id + " not found."));
    }

    public Loan returnLoan(Loan loan) {
        if (loan.isReturned())
            throw new EntityNotFoundException("Loan with ID " + loan.getId() + " has already been returned.");
        loan.setReturned(true);
        return loanRepository.save(loan);
    }

    public List<Loan> getLoanByMember(Member member) {
        return loanRepository.findAllByMemberInLoanAndReturnedFalse(member);
    }

    public void putPenalityIfNecessary(Loan loan) {
        LocalDate now = LocalDate.now();
        if (loan.getReturnDate().isBefore(now)) {
            Member member = loan.getMemberInLoan();
            member.setPenalty(member.getPenalty() == null ? now.plusDays(2) : member.getPenalty().plusDays(2));
            memberRepository.save(member);
        }
    }
}

