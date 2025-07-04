package com.mana.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class LoanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer maxLoanDays;

    @JsonIgnore
    @OneToMany(mappedBy = "loanType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;
}
