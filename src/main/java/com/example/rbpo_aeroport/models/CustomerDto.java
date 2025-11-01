package com.example.rbpo_aeroport.models;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerDto {
    private final UUID id;
    private final String name;
    private final Long passport;
}