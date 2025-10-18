package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.Passenger;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final List<Passenger> Passengers = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);


    @PostMapping
    public Passenger createPassenger(@RequestBody Passenger Passenger) {
        Passenger newPassenger = Passenger.builder()
                .id(idCounter.getAndIncrement())
                .name(Passenger.getName())
                .passport(Passenger.getPassport())
                .build();
        Passengers.add(newPassenger);
        return newPassenger;
    }


    @GetMapping
    public List<Passenger> getAllPassengers() {
        return Passengers;
    }


    @GetMapping("/{id}")
    public Passenger getPassengerById(@PathVariable Long id) {
        return Passengers.stream()
                .filter(Passenger -> Passenger.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    @DeleteMapping("/{id}")
    public String deletePassenger(@PathVariable Long id) {
        Passengers.removeIf(Passenger -> Passenger.getId().equals(id));
        return "Passenger deleted";
    }


    @PutMapping("/{id}")
    public Passenger updatePassenger(@PathVariable Long id, @RequestBody Passenger Passenger) {
        deletePassenger(id);
        Passenger updatedPassenger = Passenger.builder()
                .id(id)
                .name(Passenger.getName())
                .passport(Passenger.getPassport())
                .build();
        Passengers.add(updatedPassenger);
        return updatedPassenger;
    }
}