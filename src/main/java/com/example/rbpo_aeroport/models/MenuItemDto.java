package com.example.rbpo_aeroport.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class MenuItemDto {
    // Getters and Setters
    private Long id;
    private String name;
    private Map<String, Double> positions;
    private Long restaurantId;

    // Constructors
    public MenuItemDto() {}

    public MenuItemDto(Long id, String name, Map<String, Double> positions, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.positions = positions;
        this.restaurantId = restaurantId;
    }

}