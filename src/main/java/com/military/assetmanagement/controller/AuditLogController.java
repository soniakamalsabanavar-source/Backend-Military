package com.military.assetmanagement.controller;

import com.military.assetmanagement.model.AuditLog;
import com.military.assetmanagement.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    
    @Autowired
    private AuditService auditService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAuditLogs() {
        return auditService.getRecentLogs();
    }
}
