package com.mana.library.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subscription> subscriptions = new ArrayList<>();

    private LocalDate expirationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "memberInLoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans;

    private Double penaltyAmount = 0.0;

    private boolean active = true;

    @JsonIgnore
    @OneToMany(mappedBy = "memberInPayment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    // Méthode pour obtenir les abonnements (nécessaire pour ReservationService)
    public List<Subscription> getSubscriptions() {
        return subscriptions != null ? subscriptions : new ArrayList<>();
    }
}
