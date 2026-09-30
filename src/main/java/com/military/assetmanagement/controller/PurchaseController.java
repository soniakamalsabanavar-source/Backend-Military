package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.ActionType;
import com.military.assetmanagement.model.Base;
import com.military.assetmanagement.model.Equipment;
import com.military.assetmanagement.model.Purchase;
import com.military.assetmanagement.payload.request.PurchaseRequest;
import com.military.assetmanagement.repository.BaseRepository;
import com.military.assetmanagement.repository.EquipmentRepository;
import com.military.assetmanagement.repository.PurchaseRepository;
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
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private AssetBalanceService assetBalanceService;
    @Autowired
    private AuditService auditService;

    @GetMapping
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addPurchase(@RequestBody PurchaseRequest request) {
        Base base = baseRepository.findById(request.getBaseId()).orElseThrow();
        Equipment equipment = equipmentRepository.findById(request.getEquipmentId()).orElseThrow();
        
        Purchase purchase = new Purchase();
        purchase.setBase(base);
        purchase.setEquipment(equipment);
        purchase.setQuantity(request.getQuantity());
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setUnitCost(request.getUnitCost());
        purchase.setSupplierDetails(request.getSupplierDetails());
        
        purchaseRepository.save(purchase);
        
        assetBalanceService.updateBalanceAfterPurchase(base, equipment, request.getQuantity());
        
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        auditService.logAction(userDetails.getId(), ActionType.ADD_PURCHASE, "Purchase", purchase.getId().toString(), "Added purchase of " + request.getQuantity() + " " + equipment.getName());
        
        return ResponseEntity.ok(purchase);
    }
}
