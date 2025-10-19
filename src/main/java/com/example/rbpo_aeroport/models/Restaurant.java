package com.example.rbpo_aeroport.models;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Restaurant {
    private final Long id;
    private final String name;
    private final String location;
}
