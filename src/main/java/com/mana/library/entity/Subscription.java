package com.mana.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double amount;

    private boolean active = true;

    public enum SubscriptionType {
        MONTHLY,
        QUARTERLY,
        YEARLY
    }

    // Constructeur
    public Subscription() {
        this.startDate = LocalDateTime.now();
        this.active = true;
    }

    public Subscription(Member member, SubscriptionType type, Double amount) {
        this.member = member;
        this.type = type;
        this.amount = amount;
        this.startDate = LocalDateTime.now();
        this.active = true;

        // Calculer la date de fin selon le type
        switch (type) {
            case MONTHLY:
                this.endDate = this.startDate.plusDays(30);
                break;
            case QUARTERLY:
                this.endDate = this.startDate.plusDays(90);
                break;
            case YEARLY:
                this.endDate = this.startDate.plusDays(365);
                break;
        }
    }
}
