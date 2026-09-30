package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.*;
import com.military.assetmanagement.payload.request.AssignmentRequest;
import com.military.assetmanagement.repository.AssignmentRepository;
import com.military.assetmanagement.repository.BaseRepository;
import com.military.assetmanagement.repository.EquipmentRepository;
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
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private AssetBalanceService assetBalanceService;
    @Autowired
    private AuditService auditService;

    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addAssignment(@RequestBody AssignmentRequest request) {
        if (request.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be positive");
        }
        
        Base base = baseRepository.findById(request.getBaseId()).orElseThrow();
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId()).orElseThrow();
        
        AssetBalance balance = assetBalanceService.getOrCreateBalance(base, equipment);
        if (balance.getCurrentBalance() < request.getQuantity()) {
            return ResponseEntity.badRequest().body("Insufficient stock for assignment");
        }
        
        Assignment assignment = new Assignment();
        assignment.setBase(base);
        assignment.setEquipment(equipment);
        assignment.setPersonnel(request.getPersonnel());
        assignment.setQuantity(request.getQuantity());
        assignment.setAssignmentDate(request.getAssignmentDate());
        
        assignmentRepository.save(assignment);
        
        assetBalanceService.updateBalanceAfterAssignment(base, equipment, request.getQuantity());
        
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        auditService.logAction(userDetails.getId(), ActionType.ASSIGNMENT, "Assignment", assignment.getId().toString(), "Assigned " + request.getQuantity() + " " + equipment.getName() + " to " + request.getPersonnel());
        
        return ResponseEntity.ok(assignment);
    }
}
