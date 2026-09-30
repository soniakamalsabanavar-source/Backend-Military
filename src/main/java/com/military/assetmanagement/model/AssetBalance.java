package com.military.assetmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asset_balances", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"base_id", "equipment_id"})
})
@Data
@NoArgsConstructor
public class AssetBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_id", nullable = false)
    private Base base;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
    
    @Column(nullable = false)
    private Integer openingBalance = 0;
    
    @Column(nullable = false)
    private Integer currentBalance = 0;
    
    @Column(nullable = false)
    private Integer totalPurchases = 0;
    
    @Column(nullable = false)
    private Integer transfersIn = 0;
    
    @Column(nullable = false)
    private Integer transfersOut = 0;
    
    @Column(nullable = false)
    private Integer assignedAssets = 0;
    
    @Column(nullable = false)
    private Integer expendedAssets = 0;
}
