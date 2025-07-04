package com.mana.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String firstName;

    @Column(unique = true)
    private String email;

    private String address;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    private LocalDate expirationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "memberInLoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;

    private LocalDate penalty = null;

    private boolean active = true;

    @JsonIgnore
    @OneToMany(mappedBy = "memberInPayment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    public boolean isPenality() {
        return penalty == null || penalty.isAfter(LocalDate.now());
    }

}
