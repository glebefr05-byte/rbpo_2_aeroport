package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.Flight;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final List<Flight> Flights = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @PostMapping
    public Flight createFlight(@RequestBody Flight Flight) {
        Flight newFlight = Flight.builder()
                .id(idCounter.getAndIncrement())
                .aircraftId(Flight.getAircraftId())
                .airportDepartureId(Flight.getAirportDepartureId())
                .airportDestinationId(Flight.getAirportDestinationId())
                .time(Flight.getTime())
                .status(Flight.getStatus())
                .build();
        Flights.add(newFlight);
        return newFlight;
    }

    @GetMapping
    public List<Flight> getAllFlights() {
        return Flights;
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable Long id) {
        return Flights.stream()
                .filter(Flight -> Flight.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteFlight(@PathVariable Long id) {
        Flights.removeIf(Flight -> Flight.getId().equals(id));
        return "Flight deleted";
    }

    @PutMapping("/{id}")
    public Flight updateFlight(@PathVariable Long id, @RequestBody Flight Flight) {
        deleteFlight(id);
        Flight updatedFlight = Flight.builder()
                .id(id)
                .aircraftId(Flight.getAircraftId())
                .airportDepartureId(Flight.getAirportDepartureId())
                .airportDestinationId(Flight.getAirportDestinationId())
                .time(Flight.getTime())
                .status(Flight.getStatus())
                .build();
        Flights.add(updatedFlight);
        return updatedFlight;
    }
}