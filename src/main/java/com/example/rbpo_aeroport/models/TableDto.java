package com.example.rbpo_aeroport.models;

import lombok.Data;

import java.util.UUID;

@Data
public class TableDto {
    private final UUID id;
    private final Long restaurantId;
    private final String status;
}
