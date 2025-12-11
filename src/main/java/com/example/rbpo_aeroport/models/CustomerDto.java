package com.example.rbpo_aeroport.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDto {
    // Getters and Setters
    private Long id;
    private String name;
    private String passportId;

    // Constructors
    public CustomerDto() {}

    public CustomerDto(Long id, String name, String passportId) {
        this.id = id;
        this.name = name;
        this.passportId = passportId;
    }

}