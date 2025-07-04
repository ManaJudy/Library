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

    @Column(unique = true)
    private String copyNumber;

    @Enumerated(EnumType.STRING)
    private CopyStatus status;

    private String location;

    public enum CopyStatus {
        AVAILABLE,
        BORROWED,
        DAMAGED,
        LOST,
        RESERVED
    }

    // Constructeur
    public Copy() {
        this.status = CopyStatus.AVAILABLE;
    }

    public Copy(Book book, String copyNumber) {
        this.book = book;
        this.copyNumber = copyNumber;
        this.status = CopyStatus.AVAILABLE;
    }
}
