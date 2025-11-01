package com.example.rbpo_aeroport.services;

import com.example.rbpo_aeroport.entities.RestaurantEntity;
import com.example.rbpo_aeroport.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<RestaurantEntity> findAll() {
        return restaurantRepository.findAll();
    }

    public Optional<RestaurantEntity> findById(UUID id) {
        return restaurantRepository.findById(id);
    }

    public RestaurantEntity save(RestaurantEntity restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public RestaurantEntity update(UUID id, RestaurantEntity restaurantDetails) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(restaurantDetails.getName());
                    restaurant.setLocation(restaurantDetails.getLocation());
                    return restaurantRepository.save(restaurant);
                })
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }

    public RestaurantEntity partialUpdate(UUID id, RestaurantEntity restaurantDetails) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    if (restaurantDetails.getName() != null) {
                        restaurant.setName(restaurantDetails.getName());
                    }
                    if (restaurantDetails.getLocation() != null) {
                        restaurant.setLocation(restaurantDetails.getLocation());
                    }
                    return restaurantRepository.save(restaurant);
                })
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }

    public void deleteById(UUID id) {
        restaurantRepository.deleteById(id);
    }
}