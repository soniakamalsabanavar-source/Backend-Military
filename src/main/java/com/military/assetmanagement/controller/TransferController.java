package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.*;
import com.military.assetmanagement.payload.request.TransferRequest;
import com.military.assetmanagement.repository.BaseRepository;
import com.military.assetmanagement.repository.EquipmentRepository;
import com.military.assetmanagement.repository.TransferRepository;
import com.military.assetmanagement.security.services.UserDetailsImpl;
import com.military.assetmanagement.service.AssetBalanceService;
import com.military.assetmanagement.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private AssetBalanceService assetBalanceService;
    @Autowired
    private AuditService auditService;

    @GetMapping
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addTransfer(@RequestBody TransferRequest request) {
        if (request.getSourceBaseId().equals(request.getDestinationBaseId())) {
            return ResponseEntity.badRequest().body("Source and destination must be different");
        }
        if (request.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be positive");
        }
        
        Base sourceBase = baseRepository.findById(request.getSourceBaseId()).orElseThrow();
        Base destBase = baseRepository.findById(request.getDestinationBaseId()).orElseThrow();
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId()).orElseThrow();
        
        AssetBalance sourceBalance = assetBalanceService.getOrCreateBalance(sourceBase, equipment);
        if (sourceBalance.getCurrentBalance() < request.getQuantity()) {
            return ResponseEntity.badRequest().body("Insufficient stock at source base");
        }
        
        Transfer transfer = new Transfer();
        transfer.setTransferReference(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        transfer.setSourceBase(sourceBase);
        transfer.setDestinationBase(destBase);
        transfer.setEquipment(equipment);
        transfer.setQuantity(request.getQuantity());
        transfer.setTransferDate(request.getTransferDate());
        transfer.setRemarks(request.getRemarks());
        transfer.setStatus(TransferStatus.PENDING);
        
        transferRepository.save(transfer);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        auditService.logAction(userDetails.getId(), ActionType.TRANSFER, "Transfer", transfer.getId().toString(), "Transferred " + request.getQuantity() + " " + equipment.getName());
        
        return ResponseEntity.ok(transfer);
    }

    @PutMapping("/{id}/approve")
    @Transactional
    public ResponseEntity<?> approveTransfer(@PathVariable Long id) {
        Transfer transfer = transferRepository.findById(id).orElseThrow();
        if (transfer.getStatus() != TransferStatus.PENDING) {
            return ResponseEntity.badRequest().body("Transfer is not pending");
        }
        
        transfer.setStatus(TransferStatus.COMPLETED);
        transferRepository.save(transfer);
        
        assetBalanceService.updateBalanceAfterTransfer(transfer.getSourceBase(), transfer.getDestinationBase(), transfer.getEquipment(), transfer.getQuantity());
        
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        auditService.logAction(userDetails.getId(), ActionType.TRANSFER, "Transfer", transfer.getId().toString(), "Approved transfer");
        
        return ResponseEntity.ok(transfer);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelTransfer(@PathVariable Long id) {
        Transfer transfer = transferRepository.findById(id).orElseThrow();
        if (transfer.getStatus() != TransferStatus.PENDING) {
            return ResponseEntity.badRequest().body("Transfer is not pending");
        }
        
        transfer.setStatus(TransferStatus.CANCELLED);
        transferRepository.save(transfer);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        auditService.logAction(userDetails.getId(), ActionType.TRANSFER, "Transfer", transfer.getId().toString(), "Cancelled transfer");
        
        return ResponseEntity.ok(transfer);
    }
}
