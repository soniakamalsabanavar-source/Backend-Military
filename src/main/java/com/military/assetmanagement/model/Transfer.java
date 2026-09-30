package com.military.assetmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String transferReference;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_base_id", nullable = false)
    private Base sourceBase;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_base_id", nullable = false)
    private Base destinationBase;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private LocalDate transferDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status = TransferStatus.PENDING;
    
    private String remarks;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}
