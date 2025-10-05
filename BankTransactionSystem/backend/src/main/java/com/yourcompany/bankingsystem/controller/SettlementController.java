package com.yourcompany.bankingsystem.controller;

import com.yourcompany.bankingsystem.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/settlement")
@CrossOrigin(origins = "*")
public class SettlementController {
    
    @Autowired
    private SettlementService settlementService;
    
    @PostMapping("/daily")
    public ResponseEntity<?> performDailySettlement() {
        try {
            SettlementService.SettlementResult result = settlementService.performDailySettlement();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                java.util.Map.of("message", "Settlement failed: " + e.getMessage())
            );
        }
    }
    
    @PostMapping("/monthly")
    public ResponseEntity<?> performMonthlySettlement() {
        try {
            SettlementService.SettlementResult result = settlementService.performMonthlySettlement();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                java.util.Map.of("message", "Settlement failed: " + e.getMessage())
            );
        }
    }
    
    @PostMapping("/reconcile")
    public ResponseEntity<?> reconcilePendingTransactions() {
        try {
            int reconciledCount = settlementService.reconcilePendingTransactions();
            return ResponseEntity.ok(
                java.util.Map.of("reconciledCount", reconciledCount, 
                               "message", "Reconciliation completed")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                java.util.Map.of("message", "Reconciliation failed: " + e.getMessage())
            );
        }
    }
    
    @GetMapping("/transactions")
    public ResponseEntity<?> getSettlementTransactions(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            return ResponseEntity.ok(settlementService.getSettlementTransactions(start, end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                java.util.Map.of("message", e.getMessage())
            );
        }
    }
}
