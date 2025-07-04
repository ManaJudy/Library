package com.mana.library.service;

import com.mana.library.entity.Copy;
import com.mana.library.exceptionhandler.exception.EntityNotFoundException;
import com.mana.library.repository.CopyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CopyService {

    @Autowired
    private CopyRepository copyRepository;

    public Copy findCopyById(Long id) {
        return copyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Copy with ID " + id + " not found."));
    }
}
