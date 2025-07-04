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

    List<Loan> findAllByMemberInLoanAndReturnedFalse(Member member);
}
