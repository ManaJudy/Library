package com.mana.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
