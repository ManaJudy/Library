package com.mana.library.repository;

import com.mana.library.entity.Copy;
import com.mana.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {

    List<Copy> findByBook(Book book);

    List<Copy> findByBookAndStatus(Book book, Copy.CopyStatus status);

    List<Copy> findByStatus(Copy.CopyStatus status);

    long countByBookAndStatus(Book book, Copy.CopyStatus status);
}
