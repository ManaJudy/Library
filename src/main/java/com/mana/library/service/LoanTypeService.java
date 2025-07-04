package com.mana.library.service;

import com.mana.library.entity.LoanType;
import com.mana.library.repository.LoanTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanTypeService {

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    public List<LoanType> getAllLoanTypes() {
        return loanTypeRepository.findAll();
    }

    public LoanType findByLoanTypeById(Long id) {
        return loanTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("LoanType not found"));
    }
}
