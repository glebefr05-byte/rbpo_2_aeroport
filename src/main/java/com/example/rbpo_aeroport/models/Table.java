package com.example.rbpo_aeroport.models;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Table {
    private final Long id;
    private final Long restaurantId;
    private final String status;
}
