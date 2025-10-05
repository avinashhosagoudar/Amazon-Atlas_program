package com.yourcompany.bankingsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "accounts")
public class Account {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String accountNumber;
    
    private String customerId;
    
    private String accountType; // SB (Savings Bank) or CA (Current Account)
    
    private double balance;
    
    private boolean active = true;
    
    @CreatedDate
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    
    // Constructors
    public Account() {}
    
    public Account(String customerId, String accountType, double initialBalance) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.accountNumber = generateAccountNumber();
    }
    
    private String generateAccountNumber() {
        return System.currentTimeMillis() + "" + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", customerId='" + customerId + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", active=" + active +
                ", createdDate=" + createdDate +
                '}';
    }
}
