package com.example.rbpo_aeroport.repositories;

import com.example.rbpo_aeroport.entities.ReservationEntity;
import com.example.rbpo_aeroport.entities.ReservationEntity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {
    List<ReservationEntity> findByCustomerId(UUID customerId);
    List<ReservationEntity> findByTableId(UUID tableId);
    List<ReservationEntity> findByStatus(ReservationStatus status);
    Optional<ReservationEntity> findByIdAndCustomerId(UUID id, UUID customerId);
    List<ReservationEntity> findByTableIdAndStatus(UUID tableId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.table.id = :tableId AND r.status = 'CONFIRMED'")
    List<ReservationEntity> findConfirmedReservationsByTableId(@Param("tableId") UUID tableId);
}