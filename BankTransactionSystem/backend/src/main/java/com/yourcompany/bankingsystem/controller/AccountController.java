package com.yourcompany.bankingsystem.controller;

import com.yourcompany.bankingsystem.model.Account;
import com.yourcompany.bankingsystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, Object> request) {
        try {
            String customerId = (String) request.get("customerId");
            String accountType = (String) request.get("accountType");
            Double initialDeposit = ((Number) request.get("initialDeposit")).doubleValue();
            
            Account account = accountService.createAccount(customerId, accountType, initialDeposit);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Account>> getAccountsByCustomer(@PathVariable String customerId) {
        try {
            List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccountByNumber(@PathVariable String accountNumber) {
        try {
            Account account = accountService.getAccountByNumber(accountNumber);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @PostMapping("/close")
    public ResponseEntity<?> closeAccount(@RequestBody Map<String, String> request) {
        try {
            String accountNumber = request.get("accountNumber");
            String customerId = request.get("customerId");
            String password = request.get("password");
            
            Map<String, Object> response = accountService.closeAccount(accountNumber, customerId, password);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
    
    @GetMapping("/balance/{customerId}")
    public ResponseEntity<?> getTotalBalance(@PathVariable String customerId) {
        try {
            double totalBalance = accountService.getTotalBalanceForCustomer(customerId);
            return ResponseEntity.ok(Map.of("totalBalance", totalBalance));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
