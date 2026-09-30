package com.military.assetmanagement.repository;

import com.military.assetmanagement.model.AssetBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AssetBalanceRepository extends JpaRepository<AssetBalance, Long> {
    Optional<AssetBalance> findByBaseIdAndEquipmentId(Long baseId, Long equipmentId);
    List<AssetBalance> findByBaseId(Long baseId);
}
