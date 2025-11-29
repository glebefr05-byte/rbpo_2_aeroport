package com.example.rbpo_aeroport.models;

import lombok.Data;

import java.util.UUID;

@Data
public class ReservationDto {
    private final UUID id;
    private final Long customerId;
    private final Long tableId;
    private final Long startTime;
    private final Long duration;
    private final Double cost;
    private final String status;
}
