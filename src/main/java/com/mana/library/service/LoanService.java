package com.mana.library.service;

import com.mana.library.entity.*;
import com.mana.library.exceptionhandler.exception.EntityNotFoundException;
import com.mana.library.exceptionhandler.exception.PenaltyException;
import com.mana.library.exceptionhandler.exception.AccountExpiredException;
import com.mana.library.repository.CopyRepository;
import com.mana.library.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private CopyRepository copyRepository;

    public Loan createLoan(Member member, Book book, LoanType loanType) {
        if (member.getPenaltyAmount() > 0) throw new PenaltyException("Member has outstanding penalties.");
        if (!member.isActive()) throw new AccountExpiredException("Member account is expired.");
        Copy copy = copyRepository.findFirstCopyNotLoaned(book).orElseThrow(() -> new EntityNotFoundException("No available copies for book: " + book.getTitle()));
        Loan loan = new Loan();
        loan.setMemberInLoan(member);
        loan.setCopy(copy);
        loan.setLoanType(loanType);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(LocalDate.now().plusDays(loanType.getMaxLoanDays()));
        loan.setReturned(false);
        loanRepository.save(loan);
        return loan;
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
}

