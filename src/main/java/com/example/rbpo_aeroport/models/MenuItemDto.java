package com.example.rbpo_aeroport.models;

import lombok.Data;
import java.util.Map;
import java.util.UUID;

@Data
public class MenuItemDto {
    private final UUID id;
    private final String name;
    private final Map<String, Double> positions;
}
