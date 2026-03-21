package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.CustomerEntity;
import com.example.rbpo_aeroport.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    public List<CustomerEntity> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<CustomerEntity> getCustomerById(@PathVariable UUID id) {
        Optional<CustomerEntity> customer = customerService.findById(id);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerEntity createCustomer(@RequestBody CustomerEntity customer) {
        return customerService.save(customer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('modify')")
    public ResponseEntity<CustomerEntity> updateCustomer(@PathVariable UUID id, @RequestBody CustomerEntity customerDetails) {
        try {
            CustomerEntity updatedCustomer = customerService.update(id, customerDetails);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('modify')")
    public ResponseEntity<CustomerEntity> partialUpdateCustomer(@PathVariable UUID id, @RequestBody CustomerEntity customerDetails) {
        try {
            CustomerEntity updatedCustomer = customerService.partialUpdate(id, customerDetails);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        if (customerService.findById(id).isPresent()) {
            customerService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}