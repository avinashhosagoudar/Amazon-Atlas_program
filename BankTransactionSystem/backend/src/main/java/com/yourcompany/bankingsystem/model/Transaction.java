package com.yourcompany.bankingsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "transactions")
public class Transaction {
    
    @Id
    private String id;
    
    private String transactionId;
    
    private String fromAccountNumber;
    
    private String toAccountNumber;
    
    private double amount;
    
    private String transactionType; // DEPOSIT, WITHDRAW, TRANSFER
    
    private String status; // SUCCESS, FAILED, PENDING
    
    private String description;
    
    private double balanceAfter;
    
    @CreatedDate
    private LocalDateTime transactionDate;
    
    // Constructors
    public Transaction() {}
    
    public Transaction(String fromAccountNumber, String toAccountNumber, 
                      double amount, String transactionType, String description) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
        this.transactionId = generateTransactionId();
        this.status = "PENDING";
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + "" + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }
    
    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }
    
    public String getToAccountNumber() {
        return toAccountNumber;
    }
    
    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", fromAccountNumber='" + fromAccountNumber + '\'' +
                ", toAccountNumber='" + toAccountNumber + '\'' +
                ", amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", status='" + status + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
