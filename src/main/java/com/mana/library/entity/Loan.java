package com.mana.library.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "copy_id", nullable = false)
    private Copy copy;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member memberInLoan;

    private LocalDate loanDate;

    private LocalDate returnDate;

    private boolean returned = true;

    @ManyToOne
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanType loanType;
}
