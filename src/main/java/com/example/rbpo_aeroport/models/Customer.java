package com.example.rbpo_aeroport.models;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Customer {
    private final Long id;
    private final String name;
    private final Long passport;
}