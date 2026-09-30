package com.military.assetmanagement.service;

import com.military.assetmanagement.model.ActionType;
import com.military.assetmanagement.model.AuditLog;
import com.military.assetmanagement.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditService {
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    public void logAction(Long userId, ActionType action, String entityType, String entityId, String details) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        auditLogRepository.save(log);
    }
    
    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop100ByOrderByTimestampDesc();
    }
}
