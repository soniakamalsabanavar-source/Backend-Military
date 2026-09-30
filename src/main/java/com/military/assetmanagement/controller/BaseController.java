package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.Base;
import com.military.assetmanagement.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bases")
public class BaseController {
    
    @Autowired
    private BaseRepository baseRepository;

    @GetMapping
    public List<Base> getAllBases() {
        return baseRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Base> createBase(@RequestBody Base base) {
        return ResponseEntity.ok(baseRepository.save(base));
    }
}
