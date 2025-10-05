package com.yourcompany.bankingsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.bankingsystem.model.Transaction;
import com.yourcompany.bankingsystem.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TransactionService transactionService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testDepositMoney_Success() throws Exception {
        // Given
        Map<String, Object> request = new HashMap<>();
        request.put("accountNumber", "12345");
        request.put("amount", 1000.0);
        
        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN123");
        transaction.setStatus("SUCCESS");
        transaction.setAmount(1000.0);
        
        when(transactionService.depositMoney("12345", 1000.0)).thenReturn(transaction);
        
        // When & Then
        mockMvc.perform(post("/api/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Deposit successful"));
    }
    
    @Test
    void testDepositMoney_Failure() throws Exception {
        // Given
        Map<String, Object> request = new HashMap<>();
        request.put("accountNumber", "12345");
        request.put("amount", 1000.0);
        
        Transaction transaction = new Transaction();
        transaction.setStatus("FAILED");
        
        when(transactionService.depositMoney("12345", 1000.0)).thenReturn(transaction);
        
        // When & Then
        mockMvc.perform(post("/api/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Deposit failed"));
    }
    
    @Test
    void testWithdrawMoney_Success() throws Exception {
        // Given
        Map<String, Object> request = new HashMap<>();
        request.put("accountNumber", "12345");
        request.put("amount", 500.0);
        
        when(transactionService.withdrawMoney("12345", 500.0)).thenReturn(true);
        
        // When & Then
        mockMvc.perform(post("/api/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Withdrawal successful"));
    }
    
    @Test
    void testTransferMoney_Success() throws Exception {
        // Given
        Map<String, Object> request = new HashMap<>();
        request.put("fromAccountNumber", "12345");
        request.put("toAccountNumber", "67890");
        request.put("amount", 1000.0);
        request.put("password", "password123");
        
        Transaction mockTransaction = new Transaction("12345", "67890", 1000.0, "TRANSFER", "Transfer successful");
        when(transactionService.transferMoney("12345", "67890", 1000.0, "password123")).thenReturn(mockTransaction);
        
        // When & Then
        mockMvc.perform(post("/api/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transfer successful"));
    }
    
    @Test
    void testGetTransactionHistory() throws Exception {
        // Given
        Transaction transaction1 = new Transaction("12345", null, 1000.0, "WITHDRAW", "Test withdrawal");
        Transaction transaction2 = new Transaction(null, "12345", 2000.0, "DEPOSIT", "Test deposit");
        
        when(transactionService.getTransactionHistory("12345"))
                .thenReturn(Arrays.asList(transaction1, transaction2));
        
        // When & Then
        mockMvc.perform(get("/api/transactions/history/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
    
    @Test
    void testGetRecentTransactions() throws Exception {
        // Given
        Transaction transaction = new Transaction("12345", "67890", 500.0, "TRANSFER", "Test transfer");
        
        when(transactionService.getRecentTransactionsFromStack())
                .thenReturn(Arrays.asList(transaction));
        
        // When & Then
        mockMvc.perform(get("/api/transactions/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
    
    @Test
    void testProcessPendingTransactions() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/transactions/process-pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Pending transactions processed"));
    }
}
