package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.Airport;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airports")
public class AirportController {

    private final List<Airport> Airports = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @PostMapping
    public Airport createAirport(@RequestBody Airport Airport) {
        Airport newAirport = Airport.builder()
                .id(idCounter.getAndIncrement())
                .name(Airport.getName())
                .location(Airport.getLocation())
                .build();
        Airports.add(newAirport);
        return newAirport;
    }

    @GetMapping
    public List<Airport> getAllAirports() {
        return Airports;
    }

    @GetMapping("/{id}")
    public Airport getAirportById(@PathVariable Long id) {
        return Airports.stream()
                .filter(Airport -> Airport.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteAirport(@PathVariable Long id) {
        Airports.removeIf(Airport -> Airport.getId().equals(id));
        return "Airport deleted";
    }

    @PutMapping("/{id}")
    public Airport updateAirport(@PathVariable Long id, @RequestBody Airport Airport) {
        deleteAirport(id);
        Airport updatedAirport = Airport.builder()
                .id(id)
                .name(Airport.getName())
                .location(Airport.getLocation())
                .build();
        Airports.add(updatedAirport);
        return updatedAirport;
    }
}