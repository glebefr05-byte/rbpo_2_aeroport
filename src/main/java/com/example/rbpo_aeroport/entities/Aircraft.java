package com.example.rbpo_aeroport.entities;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Aircraft {
    private final Long id;
    private final String model;
    private final String capacity;
}
