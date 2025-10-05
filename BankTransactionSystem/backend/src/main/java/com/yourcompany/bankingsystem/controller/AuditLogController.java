package com.yourcompany.bankingsystem.controller;

import com.yourcompany.bankingsystem.model.AuditLog;
import com.yourcompany.bankingsystem.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "*")
public class AuditLogController {
    
    @Autowired
    private AuditLogService auditLogService;
    
    // Get all audit logs for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUser(@PathVariable String userId) {
        try {
            List<AuditLog> logs = auditLogService.getAuditLogsByUserId(userId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get transaction logs for a user
    @GetMapping("/user/{userId}/transactions")
    public ResponseEntity<List<AuditLog>> getTransactionLogsByUser(@PathVariable String userId) {
        try {
            List<AuditLog> allLogs = auditLogService.getAuditLogsByUserId(userId);
            List<AuditLog> transactionLogs = allLogs.stream()
                .filter(log -> log.getEntityType() != null && log.getEntityType().equals("TRANSACTION"))
                .collect(Collectors.toList());
            return ResponseEntity.ok(transactionLogs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get security logs for a user
    @GetMapping("/user/{userId}/security")
    public ResponseEntity<List<AuditLog>> getSecurityLogsByUser(@PathVariable String userId) {
        try {
            List<AuditLog> allLogs = auditLogService.getAuditLogsByUserId(userId);
            List<AuditLog> securityLogs = allLogs.stream()
                .filter(log -> {
                    String action = log.getAction();
                    return action != null && (
                        action.equals("LOGIN") || 
                        action.equals("LOGOUT") || 
                        action.equals("FAILED_LOGIN") || 
                        action.equals("PASSWORD_CHANGE") ||
                        action.equals("CUSTOMER_REGISTRATION")
                    );
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(securityLogs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get recent logs for a user (last N hours)
    @GetMapping("/user/{userId}/recent/{hours}")
    public ResponseEntity<List<AuditLog>> getRecentLogsByUser(
            @PathVariable String userId,
            @PathVariable int hours) {
        try {
            LocalDateTime since = LocalDateTime.now().minusHours(hours);
            List<AuditLog> allLogs = auditLogService.getAuditLogsByUserId(userId);
            List<AuditLog> recentLogs = allLogs.stream()
                .filter(log -> log.getTimestamp() != null && log.getTimestamp().isAfter(since))
                .collect(Collectors.toList());
            return ResponseEntity.ok(recentLogs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get audit logs by action
    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByAction(@PathVariable String action) {
        try {
            List<AuditLog> logs = auditLogService.getAuditLogsByAction(action);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get audit logs within date range
    @GetMapping("/date-range")
    public ResponseEntity<List<AuditLog>> getAuditLogsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            List<AuditLog> logs = auditLogService.getAuditLogsByDateRange(start, end);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get audit statistics
    @GetMapping("/statistics")
    public ResponseEntity<?> getAuditStatistics(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            AuditLogService.AuditStatistics stats = auditLogService.getAuditStatistics(start, end);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    // Get security logs for monitoring
    @GetMapping("/security/all")
    public ResponseEntity<List<AuditLog>> getAllSecurityLogs() {
        try {
            List<AuditLog> logs = auditLogService.getSecurityLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Get all transaction logs
    @GetMapping("/transactions/all")
    public ResponseEntity<List<AuditLog>> getAllTransactionLogs() {
        try {
            List<AuditLog> logs = auditLogService.getTransactionLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
