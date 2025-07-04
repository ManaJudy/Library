package com.mana.library.controller;

import com.mana.library.entity.LoanType;
import com.mana.library.service.LoanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loantypes")
public class LoanTypeController {

    @Autowired
    private LoanTypeService loanTypeService;

    @GetMapping
    public List<LoanType> getAllLoanTypes() {
        return loanTypeService.getAllLoanTypes();
    }
}
