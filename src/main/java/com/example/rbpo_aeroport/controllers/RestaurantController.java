package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.models.Restaurant;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping("/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        return restaurants.stream()
                .filter(Restaurant -> Restaurant.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant Restaurant) {
        Restaurant newRestaurant = Restaurant.builder()
                .id(idCounter.getAndIncrement())
                .name(Restaurant.getName())
                .location(Restaurant.getLocation())
                .build();
        restaurants.add(newRestaurant);
        return newRestaurant;
    }

    @DeleteMapping("/{id}")
    public boolean deleteRestaurant(@PathVariable Long id) {
        restaurants.removeIf(Restaurant -> Restaurant.getId().equals(id));
        return true;
    }

    @PutMapping("/{id}")
    public Restaurant updateRestaurant(@PathVariable Long id, @RequestBody Restaurant Restaurant) {
        deleteRestaurant(id);
        Restaurant updatedRestaurant = Restaurant.builder()
                .id(id)
                .name(Restaurant.getName())
                .location(Restaurant.getLocation())
                .build();
        restaurants.add(updatedRestaurant);
        return updatedRestaurant;
    }
}