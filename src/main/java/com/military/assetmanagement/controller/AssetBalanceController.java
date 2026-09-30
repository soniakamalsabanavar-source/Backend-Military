package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.AssetBalance;
import com.military.assetmanagement.repository.AssetBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/asset-balances")
public class AssetBalanceController {

    @Autowired
    private AssetBalanceRepository assetBalanceRepository;

    @GetMapping
    public List<AssetBalance> getAllAssetBalances(@RequestParam(required = false) Long baseId) {
        if (baseId != null) {
            return assetBalanceRepository.findByBaseId(baseId);
        }
        return assetBalanceRepository.findAll();
    }
}
