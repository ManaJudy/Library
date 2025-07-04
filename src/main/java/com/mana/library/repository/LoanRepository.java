package com.mana.library.repository;

import com.mana.library.entity.Loan;
import com.mana.library.entity.Book;
import com.mana.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByMemberInLoan(Member member);

    List<Loan> findByReturnDateIsNull();

    List<Loan> findByMemberInLoanAndReturnDateIsNull(Member member);

    long countByBookAndReturnDateIsNull(Book book);

    @Query("SELECT l FROM Loan l WHERE l.dueDate < :currentDate AND l.returnDate IS NULL")
    List<Loan> findOverdueLoans(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT l FROM Loan l WHERE l.memberInLoan = :member AND l.returnDate IS NULL")
    List<Loan> findActiveLoansForMember(@Param("member") Member member);
}
