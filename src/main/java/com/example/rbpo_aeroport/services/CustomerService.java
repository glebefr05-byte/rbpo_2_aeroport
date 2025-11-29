package com.example.rbpo_aeroport.services;

import com.example.rbpo_aeroport.entities.CustomerEntity;
import com.example.rbpo_aeroport.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerEntity> findAll() {
        return customerRepository.findAll();
    }

    public Optional<CustomerEntity> findById(UUID id) {
        return customerRepository.findById(id);
    }

    public CustomerEntity save(CustomerEntity customer) {
        return customerRepository.save(customer);
    }

    public CustomerEntity update(UUID id, CustomerEntity customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(customerDetails.getName());
                    customer.setPassportId(customerDetails.getPassportId());
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public CustomerEntity partialUpdate(UUID id, CustomerEntity customerDetails) {
        return customerRepository.findById(id)
                .map(customer -> {
                    if (customerDetails.getName() != null) {
                        customer.setName(customerDetails.getName());
                    }
                    if (customerDetails.getPassportId() != null) {
                        customer.setPassportId(customerDetails.getPassportId());
                    }
                    return customerRepository.save(customer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public void deleteById(UUID id) {
        customerRepository.deleteById(id);
    }
}