package com.yourcompany.bankingsystem.controller;

import com.yourcompany.bankingsystem.model.Customer;
import com.yourcompany.bankingsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String email = request.get("email");
            String password = request.get("password");
            
            Customer customer = customerService.registerCustomer(name, email, password);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            Customer customer = customerService.loginCustomer(email, password);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        try {
            String customerId = request.get("customerId");
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");
            
            boolean success = customerService.changePassword(customerId, oldPassword, newPassword);
            return ResponseEntity.ok(Map.of("success", success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage(), "success", false));
        }
    }
}
