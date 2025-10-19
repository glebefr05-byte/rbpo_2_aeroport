package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.models.Customer;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final List<Customer> customers = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customers.stream()
                .filter(Customer -> Customer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer Customer) {
        Customer newCustomer = Customer.builder()
                .id(idCounter.getAndIncrement())
                .name(Customer.getName())
                .passport(Customer.getPassport())
                .build();
        customers.add(newCustomer);
        return newCustomer;
    }

    @DeleteMapping("/{id}")
    public boolean deleteCustomer(@PathVariable Long id) {
        customers.removeIf(Customer -> Customer.getId().equals(id));
        return true;
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer Customer) {
        deleteCustomer(id);
        Customer updatedCustomer = Customer.builder()
                .id(id)
                .name(Customer.getName())
                .passport(Customer.getPassport())
                .build();
        customers.add(updatedCustomer);
        return updatedCustomer;
    }
}