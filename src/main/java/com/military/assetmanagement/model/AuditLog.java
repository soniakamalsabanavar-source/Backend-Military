package com.military.assetmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType action;
    
    @Column(nullable = false)
    private String entityType;
    
    @Column(nullable = false)
    private String entityId;
    
    @Column(columnDefinition = "TEXT")
    private String details;
    
    private LocalDateTime timestamp = LocalDateTime.now();
}
