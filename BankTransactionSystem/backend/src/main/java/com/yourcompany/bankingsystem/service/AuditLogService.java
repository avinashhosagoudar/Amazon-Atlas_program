package com.yourcompany.bankingsystem.service;

import com.yourcompany.bankingsystem.model.AuditLog;
import com.yourcompany.bankingsystem.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuditLogService {
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    // Log an action without old/new values
    public void logAction(String userId, String action, String entityType, String entityId) {
        AuditLog auditLog = new AuditLog(userId, action, entityType, entityId);
        auditLogRepository.save(auditLog);
    }
    
    // Log an action with old and new values
    public void logAction(String userId, String action, String entityType, String entityId, 
                         String oldValue, String newValue) {
        AuditLog auditLog = new AuditLog(userId, action, entityType, entityId);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);
        auditLogRepository.save(auditLog);
    }
    
    // Log an action with additional context
    public void logAction(String userId, String action, String entityType, String entityId, 
                         String oldValue, String newValue, String ipAddress, String userAgent) {
        AuditLog auditLog = new AuditLog(userId, action, entityType, entityId);
        auditLog.setOldValue(oldValue);
        auditLog.setNewValue(newValue);
        auditLog.setIpAddress(ipAddress);
        auditLog.setUserAgent(userAgent);
        auditLogRepository.save(auditLog);
    }
    
    // Get audit logs by user ID
    public List<AuditLog> getAuditLogsByUserId(String userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return auditLogRepository.findByUserId(userId, sort);
    }
    
    // Get audit logs by action
    public List<AuditLog> getAuditLogsByAction(String action) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return auditLogRepository.findByAction(action, sort);
    }
    
    // Get audit logs within date range
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return auditLogRepository.findByTimestampBetween(startDate, endDate, sort);
    }
    
    // Get security-related logs
    public List<AuditLog> getSecurityLogs() {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return auditLogRepository.findSecurityLogs(sort);
    }
    
    // Get transaction-related logs
    public List<AuditLog> getTransactionLogs() {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return auditLogRepository.findTransactionLogs(sort);
    }
    
    // Get recent audit logs for monitoring
    public List<AuditLog> getRecentAuditLogs(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return auditLogRepository.findRecentLogs(since, sort);
    }
    
    // Get compliance report logs
    public List<AuditLog> getComplianceLogs(List<String> actions, LocalDateTime startDate, LocalDateTime endDate) {
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        return auditLogRepository.findComplianceLogs(actions, startDate, endDate, sort);
    }
    
    // Get audit statistics
    public AuditStatistics getAuditStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<AuditLog> logs = getAuditLogsByDateRange(startDate, endDate);
        
        long totalLogs = logs.size();
        long loginAttempts = logs.stream().mapToLong(log -> 
            ("LOGIN".equals(log.getAction()) || "FAILED_LOGIN".equals(log.getAction())) ? 1 : 0).sum();
        long transactionLogs = logs.stream().mapToLong(log -> 
            log.getEntityType().equals("TRANSACTION") ? 1 : 0).sum();
        long failedActions = logs.stream().mapToLong(log -> 
            log.getAction().startsWith("FAILED_") ? 1 : 0).sum();
        
        return new AuditStatistics(totalLogs, loginAttempts, transactionLogs, failedActions);
    }
    
    // Inner class for audit statistics
    public static class AuditStatistics {
        private long totalLogs;
        private long loginAttempts;
        private long transactionLogs;
        private long failedActions;
        
        public AuditStatistics(long totalLogs, long loginAttempts, long transactionLogs, long failedActions) {
            this.totalLogs = totalLogs;
            this.loginAttempts = loginAttempts;
            this.transactionLogs = transactionLogs;
            this.failedActions = failedActions;
        }
        
        // Getters
        public long getTotalLogs() { return totalLogs; }
        public long getLoginAttempts() { return loginAttempts; }
        public long getTransactionLogs() { return transactionLogs; }
        public long getFailedActions() { return failedActions; }
    }
}
