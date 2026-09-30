package com.military.assetmanagement.service;

import com.military.assetmanagement.model.AssetBalance;
import com.military.assetmanagement.model.Base;
import com.military.assetmanagement.model.Equipment;
import com.military.assetmanagement.repository.AssetBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetBalanceService {
    @Autowired
    private AssetBalanceRepository assetBalanceRepository;
    
    public AssetBalance getOrCreateBalance(Base base, Equipment equipment) {
        return assetBalanceRepository.findByBaseIdAndEquipmentId(base.getId(), equipment.getId())
                .orElseGet(() -> {
                    AssetBalance newBalance = new AssetBalance();
                    newBalance.setBase(base);
                    newBalance.setEquipment(equipment);
                    return assetBalanceRepository.save(newBalance);
                });
    }
    
    public void updateBalanceAfterPurchase(Base base, Equipment equipment, int quantity) {
        AssetBalance balance = getOrCreateBalance(base, equipment);
        balance.setTotalPurchases(balance.getTotalPurchases() + quantity);
        recalculateCurrentBalance(balance);
        assetBalanceRepository.save(balance);
    }
    
    public void updateBalanceAfterTransfer(Base sourceBase, Base destinationBase, Equipment equipment, int quantity) {
        AssetBalance sourceBalance = getOrCreateBalance(sourceBase, equipment);
        sourceBalance.setTransfersOut(sourceBalance.getTransfersOut() + quantity);
        recalculateCurrentBalance(sourceBalance);
        assetBalanceRepository.save(sourceBalance);
        
        AssetBalance destBalance = getOrCreateBalance(destinationBase, equipment);
        destBalance.setTransfersIn(destBalance.getTransfersIn() + quantity);
        recalculateCurrentBalance(destBalance);
        assetBalanceRepository.save(destBalance);
    }
    
    public void updateBalanceAfterAssignment(Base base, Equipment equipment, int quantity) {
        AssetBalance balance = getOrCreateBalance(base, equipment);
        balance.setAssignedAssets(balance.getAssignedAssets() + quantity);
        recalculateCurrentBalance(balance);
        assetBalanceRepository.save(balance);
    }
    
    public void updateBalanceAfterExpenditure(Base base, Equipment equipment, int quantity) {
        AssetBalance balance = getOrCreateBalance(base, equipment);
        balance.setExpendedAssets(balance.getExpendedAssets() + quantity);
        recalculateCurrentBalance(balance);
        assetBalanceRepository.save(balance);
    }
    
    private void recalculateCurrentBalance(AssetBalance balance) {
        int netMovement = balance.getTotalPurchases() + balance.getTransfersIn() - balance.getTransfersOut();
        int closingBalance = balance.getOpeningBalance() + netMovement - balance.getAssignedAssets() - balance.getExpendedAssets();
        balance.setCurrentBalance(closingBalance);
    }
}
