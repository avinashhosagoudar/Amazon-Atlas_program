package com.yourcompany.bankingsystem.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LoggingUtil {
    
    private static final Logger transactionLogger = LoggerFactory.getLogger("TRANSACTION");
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    // Log transaction activities
    public static void logTransaction(String transactionId, String accountNumber, 
                                    String transactionType, double amount, String status) {
        String logMessage = String.format("TXN_ID: %s | ACCOUNT: %s | TYPE: %s | AMOUNT: %.2f | STATUS: %s | TIME: %s",
                                        transactionId, accountNumber, transactionType, amount, status, 
                                        LocalDateTime.now().format(formatter));
        transactionLogger.info(logMessage);
    }
    
    // Log audit activities
    public static void logAudit(String userId, String action, String entityType, 
                               String entityId, String details) {
        String logMessage = String.format("USER: %s | ACTION: %s | ENTITY: %s | ID: %s | DETAILS: %s | TIME: %s",
                                        userId, action, entityType, entityId, details, 
                                        LocalDateTime.now().format(formatter));
        auditLogger.info(logMessage);
    }
    
    // Log security events
    public static void logSecurity(String userId, String action, String ipAddress, String result) {
        String logMessage = String.format("SECURITY | USER: %s | ACTION: %s | IP: %s | RESULT: %s | TIME: %s",
                                        userId, action, ipAddress, result, 
                                        LocalDateTime.now().format(formatter));
        auditLogger.warn(logMessage);
    }
    
    // Log errors with context
    public static void logError(String component, String operation, String errorMessage, Exception e) {
        String logMessage = String.format("ERROR | COMPONENT: %s | OPERATION: %s | MESSAGE: %s | EXCEPTION: %s | TIME: %s",
                                        component, operation, errorMessage, 
                                        e != null ? e.getClass().getSimpleName() : "None",
                                        LocalDateTime.now().format(formatter));
        errorLogger.error(logMessage, e);
    }
    
    // Log performance metrics
    public static void logPerformance(String operation, long executionTime, String details) {
        String logMessage = String.format("PERFORMANCE | OPERATION: %s | TIME: %d ms | DETAILS: %s | TIMESTAMP: %s",
                                        operation, executionTime, details, 
                                        LocalDateTime.now().format(formatter));
        transactionLogger.info(logMessage);
    }
    
    // Log system events
    public static void logSystem(String event, String details) {
        String logMessage = String.format("SYSTEM | EVENT: %s | DETAILS: %s | TIME: %s",
                                        event, details, LocalDateTime.now().format(formatter));
        auditLogger.info(logMessage);
    }
    
    // Log database operations
    public static void logDatabase(String operation, String collection, String query, long executionTime) {
        String logMessage = String.format("DB | OPERATION: %s | COLLECTION: %s | QUERY: %s | TIME: %d ms | TIMESTAMP: %s",
                                        operation, collection, query, executionTime,
                                        LocalDateTime.now().format(formatter));
        transactionLogger.debug(logMessage);
    }
    
    // Log business events
    public static void logBusiness(String event, String entityType, String entityId, String details) {
        String logMessage = String.format("BUSINESS | EVENT: %s | ENTITY: %s | ID: %s | DETAILS: %s | TIME: %s",
                                        event, entityType, entityId, details,
                                        LocalDateTime.now().format(formatter));
        auditLogger.info(logMessage);
    }
}
