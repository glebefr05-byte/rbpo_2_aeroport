package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.Aircraft;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aircrafts")
public class AircraftController {

    private final List<Aircraft> Aircrafts = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @PostMapping
    public Aircraft createAircraft(@RequestBody Aircraft Aircraft) {
        Aircraft newAircraft = Aircraft.builder()
                .id(idCounter.getAndIncrement())
                .model(Aircraft.getModel())
                .capacity(Aircraft.getCapacity())
                .build();
        Aircrafts.add(newAircraft);
        return newAircraft;
    }

    @GetMapping
    public List<Aircraft> getAllAircrafts() {
        return Aircrafts;
    }

    @GetMapping("/{id}")
    public Aircraft getAircraftById(@PathVariable Long id) {
        return Aircrafts.stream()
                .filter(Aircraft -> Aircraft.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteAircraft(@PathVariable Long id) {
        Aircrafts.removeIf(Aircraft -> Aircraft.getId().equals(id));
        return "Aircraft deleted";
    }

    @PutMapping("/{id}")
    public Aircraft updateAircraft(@PathVariable Long id, @RequestBody Aircraft Aircraft) {
        deleteAircraft(id);
        Aircraft updatedAircraft = Aircraft.builder()
                .id(id)
                .model(Aircraft.getModel())
                .capacity(Aircraft.getCapacity())
                .build();
        Aircrafts.add(updatedAircraft);
        return updatedAircraft;
    }
}