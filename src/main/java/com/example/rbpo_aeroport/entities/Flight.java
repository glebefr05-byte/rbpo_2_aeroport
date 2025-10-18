package com.example.rbpo_aeroport.entities;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import java.util.List;

@Getter
@Builder
@ToString
public class Flight {
    private final Long id;
    private final Long aircraftId;
    private final Long airportDepartureId;
    private final Long airportDestinationId;
    private final Long time;
    private final String status;
}
