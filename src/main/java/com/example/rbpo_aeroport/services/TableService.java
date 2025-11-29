package com.example.rbpo_aeroport.services;

import com.example.rbpo_aeroport.entities.ReservationEntity;
import com.example.rbpo_aeroport.entities.TableEntity;
import com.example.rbpo_aeroport.entities.TableEntity.TableStatus;
import com.example.rbpo_aeroport.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ReservationService reservationService;

    public List<TableEntity> findAll() {
        return tableRepository.findAll();
    }

    public Optional<TableEntity> findById(UUID id) {
        return tableRepository.findById(id);
    }

    public List<TableEntity> findByRestaurantId(UUID restaurantId) {
        return tableRepository.findByRestaurantId(restaurantId);
    }

    public List<TableEntity> findByRestaurantIdAndStatus(UUID restaurantId, TableStatus status) {
        return tableRepository.findByRestaurantIdAndStatus(restaurantId, status);
    }

    public TableEntity save(TableEntity table) {
        return tableRepository.save(table);
    }

    public TableEntity update(UUID id, TableEntity tableDetails) {
        return tableRepository.findById(id)
                .map(table -> {
                    table.setRestaurant(tableDetails.getRestaurant());
                    table.setStatus(tableDetails.getStatus());
                    return tableRepository.save(table);
                })
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }

    public TableEntity partialUpdate(UUID id, TableEntity tableDetails) {
        return tableRepository.findById(id)
                .map(table -> {
                    if (tableDetails.getRestaurant() != null) {
                        table.setRestaurant(tableDetails.getRestaurant());
                    }
                    if (tableDetails.getStatus() != null) {
                        table.setStatus(tableDetails.getStatus());
                    }
                    return tableRepository.save(table);
                })
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }

    public void deleteById(UUID id) {
        tableRepository.deleteById(id);
    }

    @Transactional
    public TableEntity changeTableStatus(UUID tableId, TableEntity.TableStatus newStatus) {
        Optional<TableEntity> tableOpt = tableRepository.findById(tableId);
        if (tableOpt.isEmpty()) {
            throw new RuntimeException("Table not found with id: " + tableId);
        }

        TableEntity table = tableOpt.get();
        table.setStatus(newStatus);

        // Если статус меняется на недоступный, отменяем все бронирования
        if (newStatus != TableEntity.TableStatus.AVAILABLE) {
            cancelAllTableReservations(tableId);
        }

        return tableRepository.save(table);
    }

    @Transactional
    public void cancelAllTableReservations(UUID tableId) {
        List<ReservationEntity> tableReservations = reservationService.findByTableId(tableId);

        for (ReservationEntity reservation : tableReservations) {
            if (reservation.getStatus() == ReservationEntity.ReservationStatus.CONFIRMED ||
                    reservation.getStatus() == ReservationEntity.ReservationStatus.PENDING) {
                reservation.setStatus(ReservationEntity.ReservationStatus.CANCELLED);
                reservationService.save(reservation);
            }
        }
    }
}