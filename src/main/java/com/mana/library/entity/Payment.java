package com.mana.library.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String wayOfPayment;
    private Double amount;
    private LocalDate paymentDate;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member memberInPayment;
}
