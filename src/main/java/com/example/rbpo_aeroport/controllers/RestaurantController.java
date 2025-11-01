package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.RestaurantEntity;
import com.example.rbpo_aeroport.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantEntity> getRestaurantById(@PathVariable UUID id) {
        Optional<RestaurantEntity> restaurant = restaurantService.findById(id);
        return restaurant.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RestaurantEntity createRestaurant(@RequestBody RestaurantEntity restaurant) {
        return restaurantService.save(restaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantEntity> updateRestaurant(@PathVariable UUID id, @RequestBody RestaurantEntity restaurantDetails) {
        try {
            RestaurantEntity updatedRestaurant = restaurantService.update(id, restaurantDetails);
            return ResponseEntity.ok(updatedRestaurant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantEntity> partialUpdateRestaurant(@PathVariable UUID id, @RequestBody RestaurantEntity restaurantDetails) {
        try {
            RestaurantEntity updatedRestaurant = restaurantService.partialUpdate(id, restaurantDetails);
            return ResponseEntity.ok(updatedRestaurant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        if (restaurantService.findById(id).isPresent()) {
            restaurantService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}