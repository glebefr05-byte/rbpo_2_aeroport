package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.ReservationEntity;
import com.example.rbpo_aeroport.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<ReservationEntity> getAllReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationEntity> getReservationById(@PathVariable UUID id) {
        Optional<ReservationEntity> reservation = reservationService.findById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public List<ReservationEntity> getReservationsByCustomer(@PathVariable UUID customerId) {
        return reservationService.findByCustomerId(customerId);
    }

    @GetMapping("/table/{tableId}")
    public List<ReservationEntity> getReservationsByTable(@PathVariable UUID tableId) {
        return reservationService.findByTableId(tableId);
    }

    @PostMapping
    public ReservationEntity createReservation(@RequestBody ReservationEntity reservation) {
        return reservationService.save(reservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationEntity> updateReservation(@PathVariable UUID id, @RequestBody ReservationEntity reservationDetails) {
        try {
            ReservationEntity updatedReservation = reservationService.update(id, reservationDetails);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationEntity> partialUpdateReservation(@PathVariable UUID id, @RequestBody ReservationEntity reservationDetails) {
        try {
            ReservationEntity updatedReservation = reservationService.partialUpdate(id, reservationDetails);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        if (reservationService.findById(id).isPresent()) {
            reservationService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/tablecheck")
    public ResponseEntity<Map<String, Object>> checkTableAvailability(
            @RequestBody TableCheckRequest request) {
        try {
            int result = reservationService.checkTableAvailability(
                    request.getTableId(),
                    request.getStartTime(),
                    request.getDuration()
            );

            return ResponseEntity.ok(Map.of(
                    "available", result == 1,
                    "resultCode", result,
                    "message", getAvailabilityMessage(result)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/reservetable")
    public ResponseEntity<?> reserveTable(@RequestBody ReserveTableRequest request) {
        try {
            ReservationEntity reservation = reservationService.reserveTable(
                    request.getTableId(),
                    request.getCustomerId(),
                    request.getStartTime(),
                    request.getDuration()
            );

            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/{reservationId}/preorder")
    public ResponseEntity<?> addPreOrder(
            @PathVariable UUID reservationId,
            @RequestBody PreOrderRequest request) {
        try {
            ReservationEntity reservation = reservationService.addPreOrder(
                    reservationId,
                    request.getMenuItemId(),
                    request.getPositionName()
            );

            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    private String getAvailabilityMessage(int resultCode) {
        return switch (resultCode) {
            case 1 -> "Table is available";
            case 0 -> "Table is not available";
            case -1 -> "Table not found";
            default -> "Unknown status";
        };
    }

    // DTO классы для запросов
    public static class TableCheckRequest {
        private UUID tableId;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime startTime;
        private Integer duration;

        // Getters and Setters
        public UUID getTableId() { return tableId; }
        public void setTableId(UUID tableId) { this.tableId = tableId; }
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public Integer getDuration() { return duration; }
        public void setDuration(Integer duration) { this.duration = duration; }
    }

    public static class ReserveTableRequest {
        private UUID tableId;
        private UUID customerId;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime startTime;
        private Integer duration;

        // Getters and Setters
        public UUID getTableId() { return tableId; }
        public void setTableId(UUID tableId) { this.tableId = tableId; }
        public UUID getCustomerId() { return customerId; }
        public void setCustomerId(UUID customerId) { this.customerId = customerId; }
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public Integer getDuration() { return duration; }
        public void setDuration(Integer duration) { this.duration = duration; }
    }

    public static class PreOrderRequest {
        private UUID menuItemId;
        private String positionName;

        // Getters and Setters
        public UUID getMenuItemId() { return menuItemId; }
        public void setMenuItemId(UUID menuItemId) { this.menuItemId = menuItemId; }
        public String getPositionName() { return positionName; }
        public void setPositionName(String positionName) { this.positionName = positionName; }
    }
}