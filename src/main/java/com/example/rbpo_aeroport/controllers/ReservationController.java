package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.models.Reservation;
import com.example.rbpo_aeroport.models.Table;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final TableController TableController;

    public ReservationController(TableController TableController) {
        this.TableController = TableController;
    }

    @GetMapping("/{id}")
    public Reservation getBookingById(@PathVariable Long id) {
        return reservations.stream()
                .filter(Reservation -> Reservation.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Reservation createBooking(@RequestBody Reservation reservationRequest) {
        Table Table = TableController.getTableById(reservationRequest.getTableId());

        if (Table == null) {
            throw new RuntimeException("Table not found with id: " + reservationRequest.getTableId());
        }
        else if (Objects.equals(Table.getStatus(), "taken")){
            throw new RuntimeException("Table taken");
        }

        Reservation newReservation = Reservation.builder()
                .id(idCounter.getAndIncrement())
                .tableId(reservationRequest.getTableId())
                .customerId(reservationRequest.getCustomerId())
                .startTime(reservationRequest.getStartTime())
                .duration(reservationRequest.getDuration())
                .cost(reservationRequest.getCost())
                .build();
        reservations.add(newReservation);
        return newReservation;
    }

    @DeleteMapping("/{id}")
    public boolean deleteBooking(@PathVariable Long id) {
        reservations.removeIf(Reservation -> Reservation.getId().equals(id));
        return true;
    }

    @PutMapping("/{id}")
    public Reservation updateBooking(@PathVariable Long id, @RequestBody Reservation Reservation) {
        deleteBooking(id);
        Reservation updatedReservation = Reservation.builder()
                .id(id)
                .tableId(Reservation.getTableId())
                .customerId(Reservation.getCustomerId())
                .startTime(Reservation.getStartTime())
                .duration(Reservation.getDuration())
                .cost(Reservation.getCost())
                .build();
        reservations.add(updatedReservation);
        return updatedReservation;
    }
}