package com.yourcompany.bankingsystem.repository;

import com.yourcompany.bankingsystem.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    
    // Find customer by email
    Optional<Customer> findByEmail(String email);
    
    // Find customer by email and password
    Optional<Customer> findByEmailAndPassword(String email, String password);
    
    // Find active customers
    @Query("{'active': true}")
    List<Customer> findActiveCustomers();
    
    // Find customers by name containing (case insensitive)
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find customers created after a specific date
    @Query("{'createdDate': {$gte: ?0}}")
    List<Customer> findCustomersCreatedAfter(java.time.LocalDateTime date);
    
    // Count active customers
    @Query(value = "{'active': true}", count = true)
    long countActiveCustomers();
}
