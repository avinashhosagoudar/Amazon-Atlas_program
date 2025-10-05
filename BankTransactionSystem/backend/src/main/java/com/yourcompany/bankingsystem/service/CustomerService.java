package com.yourcompany.bankingsystem.service;

import com.yourcompany.bankingsystem.model.Customer;
import com.yourcompany.bankingsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AuditLogService auditLogService;
    
    // Register a new customer
    public Customer registerCustomer(String name, String email, String password) {
        // Validate input
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        
        if (email == null || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        
        // Check if email already exists
        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        // Create new customer
        Customer customer = new Customer(name.trim(), email.toLowerCase(), hashPassword(password));
        Customer savedCustomer = customerRepository.save(customer);
        
        // Log audit
        auditLogService.logAction(savedCustomer.getId(), "CUSTOMER_REGISTRATION", 
                                 "CUSTOMER", savedCustomer.getId());
        
        return savedCustomer;
    }
    
    // Customer login
    public Customer loginCustomer(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        
        Optional<Customer> customerOpt = customerRepository.findByEmailAndPassword(
            email.toLowerCase(), hashPassword(password));
        
        if (customerOpt.isPresent() && customerOpt.get().isActive()) {
            Customer customer = customerOpt.get();
            
            // Log successful login
            auditLogService.logAction(customer.getId(), "LOGIN", "CUSTOMER", customer.getId());
            
            return customer;
        } else {
            // Log failed login attempt
            auditLogService.logAction("UNKNOWN", "FAILED_LOGIN", "CUSTOMER", email);
            return null;
        }
    }
    
    // Get customer by ID
    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }
    
    // Get customer by email
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }
    
    // Update customer information
    public Customer updateCustomer(String customerId, String name, String email) {
        Customer customer = getCustomerById(customerId);
        
        String oldValue = customer.toString();
        
        if (name != null && !name.trim().isEmpty()) {
            customer.setName(name.trim());
        }
        
        if (email != null && isValidEmail(email)) {
            // Check if new email already exists (excluding current customer)
            Optional<Customer> existingCustomer = customerRepository.findByEmail(email.toLowerCase());
            if (existingCustomer.isPresent() && !existingCustomer.get().getId().equals(customerId)) {
                throw new IllegalArgumentException("Email already in use");
            }
            customer.setEmail(email.toLowerCase());
        }
        
        Customer updatedCustomer = customerRepository.save(customer);
        
        // Log audit
        auditLogService.logAction(customerId, "CUSTOMER_UPDATE", "CUSTOMER", customerId, 
                                 oldValue, updatedCustomer.toString());
        
        return updatedCustomer;
    }
    
    // Change password
    public boolean changePassword(String customerId, String oldPassword, String newPassword) {
        Customer customer = getCustomerById(customerId);
        
        // Verify old password
        if (!customer.getPassword().equals(hashPassword(oldPassword))) {
            auditLogService.logAction(customerId, "FAILED_PASSWORD_CHANGE", "CUSTOMER", customerId);
            return false;
        }
        
        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }
        
        // Update password
        customer.setPassword(hashPassword(newPassword));
        customerRepository.save(customer);
        
        // Log audit
        auditLogService.logAction(customerId, "PASSWORD_CHANGE", "CUSTOMER", customerId);
        
        return true;
    }
    
    // Deactivate customer account
    public boolean deactivateCustomer(String customerId) {
        Customer customer = getCustomerById(customerId);
        customer.setActive(false);
        customerRepository.save(customer);
        
        // Log audit
        auditLogService.logAction(customerId, "CUSTOMER_DEACTIVATION", "CUSTOMER", customerId);
        
        return true;
    }
    
    // Get all active customers
    public List<Customer> getAllActiveCustomers() {
        return customerRepository.findActiveCustomers();
    }
    
    // Search customers by name
    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Get customer statistics
    public long getActiveCustomerCount() {
        return customerRepository.countActiveCustomers();
    }
    
    // Validate email format
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
    
    // Verify customer password by ID
    public boolean verifyCustomerPassword(String customerId, String password) {
        if (customerId == null || password == null || password.trim().isEmpty()) {
            return false;
        }
        
        try {
            Optional<Customer> customerOpt = customerRepository.findById(customerId);
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                // Check if the hashed password matches
                return customer.getPassword().equals(hashPassword(password));
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Simple password hashing (in production, use BCrypt or similar)
    private String hashPassword(String password) {
        // This is a simple hash for demo purposes
        // In production, use BCrypt or similar secure hashing
        return "HASH_" + password.hashCode();
    }
}
