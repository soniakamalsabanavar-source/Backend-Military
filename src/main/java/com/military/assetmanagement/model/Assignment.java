package com.military.assetmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Data
@NoArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_id", nullable = false)
    private Base base;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
    
    @Column(nullable = false)
    private String personnel;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private LocalDate assignmentDate;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}
