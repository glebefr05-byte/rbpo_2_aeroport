package com.example.rbpo_aeroport.models;

import lombok.Data;

import java.util.UUID;

@Data
public class RestaurantDto {
    private final UUID id;
    private final String name;
    private final String location;
}
