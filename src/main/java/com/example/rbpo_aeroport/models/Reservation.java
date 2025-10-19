package com.example.rbpo_aeroport.models;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Reservation {
    private final Long id;
    private final Long customerId;
    private final Long tableId;
    private final Long startTime;
    private final Long duration;
    private final Double cost;
    private final String status;
}
