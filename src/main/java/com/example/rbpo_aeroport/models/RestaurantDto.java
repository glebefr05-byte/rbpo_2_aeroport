package com.example.rbpo_aeroport.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RestaurantDto {
    // Getters and Setters
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    private List<Long> tableIds;
    private List<Long> menuItemIds;

    // Constructors
    public RestaurantDto() {}

    public RestaurantDto(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

}