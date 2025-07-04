package com.mana.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mana.library.entity.LoanType;

@Repository
public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
}
