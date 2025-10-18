package com.example.rbpo_aeroport.entities;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Passenger {
    private final Long id;
    private final String name;
    private final Long passport;
}