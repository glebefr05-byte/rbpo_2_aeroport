package com.example.rbpo_aeroport.repositories;

import com.example.rbpo_aeroport.entities.TableEntity;
import com.example.rbpo_aeroport.entities.TableEntity.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, UUID> {
    List<TableEntity> findByRestaurantId(UUID restaurantId);
    List<TableEntity> findByRestaurantIdAndStatus(UUID restaurantId, TableStatus status);
}