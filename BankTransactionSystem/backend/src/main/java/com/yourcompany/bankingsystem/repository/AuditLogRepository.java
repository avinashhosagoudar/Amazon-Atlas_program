package com.yourcompany.bankingsystem.repository;

import com.yourcompany.bankingsystem.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    
    // Find audit logs by user ID
    List<AuditLog> findByUserId(String userId, Sort sort);
    
    // Find audit logs by action
    List<AuditLog> findByAction(String action, Sort sort);
    
    // Find audit logs by entity type
    List<AuditLog> findByEntityType(String entityType, Sort sort);
    
    // Find audit logs by entity ID
    List<AuditLog> findByEntityId(String entityId, Sort sort);
    
    // Find audit logs within date range
    @Query("{'timestamp': {$gte: ?0, $lte: ?1}}")
    List<AuditLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Sort sort);
    
    // Find audit logs by user and action
    List<AuditLog> findByUserIdAndAction(String userId, String action, Sort sort);
    
    // Find audit logs by user within date range
    @Query("{'userId': ?0, 'timestamp': {$gte: ?1, $lte: ?2}}")
    List<AuditLog> findByUserIdAndTimestampBetween(String userId, LocalDateTime startDate, LocalDateTime endDate, Sort sort);
    
    // Find audit logs by IP address
    List<AuditLog> findByIpAddress(String ipAddress, Sort sort);
    
    // Find recent audit logs for monitoring
    @Query("{'timestamp': {$gte: ?0}}")
    List<AuditLog> findRecentLogs(LocalDateTime since, Sort sort);
    
    // Count logs by action
    long countByAction(String action);
    
    // Count logs by user
    long countByUserId(String userId);
    
    // Find security-related logs
    @Query("{'action': {$in: ['LOGIN', 'LOGOUT', 'FAILED_LOGIN', 'PASSWORD_CHANGE']}}")
    List<AuditLog> findSecurityLogs(Sort sort);
    
    // Find transaction-related logs
    @Query("{'entityType': 'TRANSACTION'}")
    List<AuditLog> findTransactionLogs(Sort sort);
    
    // Find logs for compliance reporting
    @Query("{'action': {$in: ?0}, 'timestamp': {$gte: ?1, $lte: ?2}}")
    List<AuditLog> findComplianceLogs(List<String> actions, LocalDateTime startDate, LocalDateTime endDate, Sort sort);
}
