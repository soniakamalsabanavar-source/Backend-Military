package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.Equipment;
import com.military.assetmanagement.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    
    @Autowired
    private EquipmentRepository equipmentRepository;

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        return ResponseEntity.ok(equipmentRepository.save(equipment));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @RequestBody Equipment equipmentDetails) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow();
        equipment.setName(equipmentDetails.getName());
        equipment.setCategory(equipmentDetails.getCategory());
        equipment.setDescription(equipmentDetails.getDescription());
        return ResponseEntity.ok(equipmentRepository.save(equipment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) {
        equipmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
