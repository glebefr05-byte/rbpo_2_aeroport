package com.example.rbpo_aeroport.entities;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Booking {
    private final Long id;
    private final Long passengerId;
    private final Long flightId;
    private final Double cost;
    private final String status;
}
