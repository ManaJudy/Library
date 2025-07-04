package com.mana.library.repository;

import com.mana.library.entity.Copy;
import com.mana.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {

    @Query("SELECT c FROM Copy c WHERE c.book = :book AND c.id NOT IN (SELECT l.copy.id FROM Loan l WHERE l.returned = false)")
    Optional<Copy> findFirstCopyNotLoaned(@Param("book") Book book);
}
