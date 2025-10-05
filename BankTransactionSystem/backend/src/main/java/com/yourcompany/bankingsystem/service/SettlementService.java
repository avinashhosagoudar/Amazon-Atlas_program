package com.yourcompany.bankingsystem.service;

import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SettlementService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AuditLogService auditLogService;
    
    // Daily settlement process
    public SettlementResult performDailySettlement() {
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        LocalDateTime startOfDay = endOfDay.withHour(0).withMinute(0).withSecond(0);
        
        return performSettlement(startOfDay, endOfDay, "DAILY");
    }
    
    // Monthly settlement process
    public SettlementResult performMonthlySettlement() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
                                     .withHour(23).withMinute(59).withSecond(59);
        
        return performSettlement(startOfMonth, endOfMonth, "MONTHLY");
    }
    
    // Generic settlement process
    @Transactional
    public SettlementResult performSettlement(LocalDateTime startDate, LocalDateTime endDate, String settlementType) {
        try {
            // Get all successful transactions for the period
            List<Transaction> transactions = transactionRepository
                .findSuccessfulTransactionsForSettlement(startDate, endDate);
            
            // Calculate settlement metrics
            long totalTransactions = transactions.size();
            double totalAmount = transactions.stream()
                                           .mapToDouble(Transaction::getAmount)
                                           .sum();
            
            long depositCount = transactions.stream()
                                          .mapToLong(t -> "DEPOSIT".equals(t.getTransactionType()) ? 1 : 0)
                                          .sum();
            
            long withdrawalCount = transactions.stream()
                                             .mapToLong(t -> "WITHDRAW".equals(t.getTransactionType()) ? 1 : 0)
                                             .sum();
            
            long transferCount = transactions.stream()
                                           .mapToLong(t -> "TRANSFER".equals(t.getTransactionType()) ? 1 : 0)
                                           .sum();
            
            double depositAmount = transactions.stream()
                                             .filter(t -> "DEPOSIT".equals(t.getTransactionType()))
                                             .mapToDouble(Transaction::getAmount)
                                             .sum();
            
            double withdrawalAmount = transactions.stream()
                                                .filter(t -> "WITHDRAW".equals(t.getTransactionType()))
                                                .mapToDouble(Transaction::getAmount)
                                                .sum();
            
            double transferAmount = transactions.stream()
                                              .filter(t -> "TRANSFER".equals(t.getTransactionType()))
                                              .mapToDouble(Transaction::getAmount)
                                              .sum();
            
            // Create settlement result
            SettlementResult result = new SettlementResult(
                settlementType, startDate, endDate, totalTransactions, totalAmount,
                depositCount, withdrawalCount, transferCount,
                depositAmount, withdrawalAmount, transferAmount
            );
            
            // Log settlement completion
            auditLogService.logAction("SYSTEM", "SETTLEMENT_COMPLETED", "SETTLEMENT", settlementType);
            
            return result;
            
        } catch (Exception e) {
            // Log settlement failure
            auditLogService.logAction("SYSTEM", "SETTLEMENT_FAILED", "SETTLEMENT", 
                                     settlementType + ": " + e.getMessage());
            throw new RuntimeException("Settlement failed: " + e.getMessage(), e);
        }
    }
    
    // Get settlement history
    public List<Transaction> getSettlementTransactions(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findSuccessfulTransactionsForSettlement(startDate, endDate);
    }
    
    // Reconcile pending transactions
    @Transactional
    public int reconcilePendingTransactions() {
        List<Transaction> pendingTransactions = transactionRepository.findPendingTransactions();
        int reconciledCount = 0;
        
        for (Transaction transaction : pendingTransactions) {
            // Check if transaction is older than 24 hours
            if (transaction.getTransactionDate().isBefore(LocalDateTime.now().minusHours(24))) {
                transaction.setStatus("FAILED");
                transaction.setDescription("Transaction timed out during settlement");
                transactionRepository.save(transaction);
                reconciledCount++;
                
                auditLogService.logAction("SYSTEM", "TRANSACTION_TIMEOUT", "TRANSACTION", 
                                         transaction.getTransactionId());
            }
        }
        
        return reconciledCount;
    }
    
    // Settlement result class
    public static class SettlementResult {
        private String settlementType;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private long totalTransactions;
        private double totalAmount;
        private long depositCount;
        private long withdrawalCount;
        private long transferCount;
        private double depositAmount;
        private double withdrawalAmount;
        private double transferAmount;
        private LocalDateTime settlementTime;
        
        public SettlementResult(String settlementType, LocalDateTime startDate, LocalDateTime endDate,
                              long totalTransactions, double totalAmount, long depositCount,
                              long withdrawalCount, long transferCount, double depositAmount,
                              double withdrawalAmount, double transferAmount) {
            this.settlementType = settlementType;
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalTransactions = totalTransactions;
            this.totalAmount = totalAmount;
            this.depositCount = depositCount;
            this.withdrawalCount = withdrawalCount;
            this.transferCount = transferCount;
            this.depositAmount = depositAmount;
            this.withdrawalAmount = withdrawalAmount;
            this.transferAmount = transferAmount;
            this.settlementTime = LocalDateTime.now();
        }
        
        // Getters
        public String getSettlementType() { return settlementType; }
        public LocalDateTime getStartDate() { return startDate; }
        public LocalDateTime getEndDate() { return endDate; }
        public long getTotalTransactions() { return totalTransactions; }
        public double getTotalAmount() { return totalAmount; }
        public long getDepositCount() { return depositCount; }
        public long getWithdrawalCount() { return withdrawalCount; }
        public long getTransferCount() { return transferCount; }
        public double getDepositAmount() { return depositAmount; }
        public double getWithdrawalAmount() { return withdrawalAmount; }
        public double getTransferAmount() { return transferAmount; }
        public LocalDateTime getSettlementTime() { return settlementTime; }
        
        @Override
        public String toString() {
            return String.format(
                "Settlement Report [%s]\n" +
                "Period: %s to %s\n" +
                "Total Transactions: %d (Amount: INR %.2f)\n" +
                "Deposits: %d (Amount: INR %.2f)\n" +
                "Withdrawals: %d (Amount: INR %.2f)\n" +
                "Transfers: %d (Amount: INR %.2f)\n" +
                "Settlement Time: %s",
                settlementType, startDate, endDate, totalTransactions, totalAmount,
                depositCount, depositAmount, withdrawalCount, withdrawalAmount,
                transferCount, transferAmount, settlementTime
            );
        }
    }
}
