package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.AssetBalance;
import com.military.assetmanagement.payload.response.DashboardSummaryResponse;
import com.military.assetmanagement.repository.AssetBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private AssetBalanceRepository assetBalanceRepository;

    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary(@RequestParam(required = false) Long baseId) {
        List<AssetBalance> balances;
        if (baseId != null) {
            balances = assetBalanceRepository.findByBaseId(baseId);
        } else {
            balances = assetBalanceRepository.findAll();
        }

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        
        int totalOpening = 0;
        int totalPurchases = 0;
        int totalTransfersIn = 0;
        int totalTransfersOut = 0;
        int totalAssigned = 0;
        int totalExpended = 0;
        int totalClosing = 0;
        
        for (AssetBalance balance : balances) {
            totalOpening += balance.getOpeningBalance();
            totalPurchases += balance.getTotalPurchases();
            totalTransfersIn += balance.getTransfersIn();
            totalTransfersOut += balance.getTransfersOut();
            totalAssigned += balance.getAssignedAssets();
            totalExpended += balance.getExpendedAssets();
            totalClosing += balance.getCurrentBalance();
        }
        
        int netMovement = totalPurchases + totalTransfersIn - totalTransfersOut;
        
        response.setTotalOpeningBalance(totalOpening);
        response.setTotalClosingBalance(totalClosing);
        response.setNetMovement(netMovement);
        response.setTotalPurchases(totalPurchases);
        response.setTotalTransfersIn(totalTransfersIn);
        response.setTotalTransfersOut(totalTransfersOut);
        response.setTotalAssignedAssets(totalAssigned);
        response.setTotalExpendedAssets(totalExpended);
        
        return response;
    }
}
