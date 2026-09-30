package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.*;
import com.military.assetmanagement.payload.request.ExpenditureRequest;
import com.military.assetmanagement.repository.BaseRepository;
import com.military.assetmanagement.repository.EquipmentRepository;
import com.military.assetmanagement.repository.ExpenditureRepository;
import com.military.assetmanagement.security.services.UserDetailsImpl;
import com.military.assetmanagement.service.AssetBalanceService;
import com.military.assetmanagement.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController {

    @Autowired
    private ExpenditureRepository expenditureRepository;
    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private AssetBalanceService assetBalanceService;
    @Autowired
    private AuditService auditService;

    @GetMapping
    public List<Expenditure> getAllExpenditures() {
        return expenditureRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addExpenditure(@RequestBody ExpenditureRequest request) {
        if (request.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be positive");
        }
        
        Base base = baseRepository.findById(request.getBaseId()).orElseThrow();
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId()).orElseThrow();
        
        AssetBalance balance = assetBalanceService.getOrCreateBalance(base, equipment);
        if (balance.getCurrentBalance() < request.getQuantity()) {
            return ResponseEntity.badRequest().body("Insufficient stock for expenditure");
        }
        
        Expenditure expenditure = new Expenditure();
        expenditure.setBase(base);
        expenditure.setEquipment(equipment);
        expenditure.setQuantity(request.getQuantity());
        expenditure.setExpenditureDate(request.getExpenditureDate());
        expenditure.setRemarks(request.getRemarks());
        
        expenditureRepository.save(expenditure);
        
        assetBalanceService.updateBalanceAfterExpenditure(base, equipment, request.getQuantity());
        
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        auditService.logAction(userDetails.getId(), ActionType.EXPENDITURE, "Expenditure", expenditure.getId().toString(), "Expended " + request.getQuantity() + " " + equipment.getName());
        
        return ResponseEntity.ok(expenditure);
    }
}
