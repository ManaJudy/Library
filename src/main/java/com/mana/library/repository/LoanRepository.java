package com.mana.library.repository;

import com.mana.library.entity.Loan;
import com.mana.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByMemberAndReturnedFalse(Member member);
}
