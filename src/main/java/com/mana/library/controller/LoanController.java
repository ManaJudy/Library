package com.mana.library.controller;

import com.mana.library.entity.Book;
import com.mana.library.entity.LoanType;
import com.mana.library.entity.Member;
import com.mana.library.repository.LoanTypeRepository;
import com.mana.library.service.BookService;
import com.mana.library.service.LoanService;
import com.mana.library.entity.Loan;
import com.mana.library.service.LoanTypeService;
import com.mana.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanTypeService loanTypeService;


    @PostMapping("/create")
    public ResponseEntity<Loan> createLoan(@RequestParam Long memberId, @RequestParam Long bookId, @RequestParam Long loanTypeId) {
        Member member = memberService.findMemberById(memberId);
        Book book = bookService.findBookById(bookId);
        LoanType loanType = loanTypeService.findByLoanTypeById(loanTypeId);
        Loan loan = loanService.createLoan(member, book, loanType);
        loan = loanService.extendLoan(loan);
        return ResponseEntity.ok(loan);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<Loan> returnLoan(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        Loan returnedLoan = loanService.returnLoan(loan);
        loanService.putPenalityIfNecessary(loan);
        return ResponseEntity.ok(returnedLoan);
    }

    @GetMapping("/of-member/{id}")
    public ResponseEntity<List<Loan>> getLoanByMemberId(@PathVariable Long id) {
        Member member = memberService.findMemberById(id);
        List<Loan> loans = loanService.getLoanByMember(member);
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/extend")
    public ResponseEntity<Loan> extend(@RequestBody Map<String, Object> data) {
        Loan loan = loanService.getLoanById(Long.valueOf((String) data.get("id")));
        loan = loanService.extendLoan(loan);
        return ResponseEntity.ok(loan);
    }
}
