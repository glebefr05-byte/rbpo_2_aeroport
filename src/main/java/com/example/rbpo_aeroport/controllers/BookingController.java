package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.Booking;
import com.example.rbpo_aeroport.entities.Flight;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final List<Booking> Bookings = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final FlightController FlightController;

    public BookingController(FlightController FlightController) {
        this.FlightController = FlightController;
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking BookingRequest) {
        Flight Flight = FlightController.getFlightById(BookingRequest.getFlightId());

        if (Flight == null) {
            throw new RuntimeException("Flight not found with id: " + BookingRequest.getFlightId());
        }
        else if (Objects.equals(Flight.getStatus(), "full")){
            throw new RuntimeException("Flight capacity full");
        }

        Booking newBooking = Booking.builder()
                .id(idCounter.getAndIncrement())
                .flightId(BookingRequest.getFlightId())
                .passengerId(BookingRequest.getPassengerId())
                .cost(BookingRequest.getCost())
                .build();
        Bookings.add(newBooking);
        return newBooking;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return Bookings;
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return Bookings.stream()
                .filter(Booking -> Booking.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        Bookings.removeIf(Booking -> Booking.getId().equals(id));
        return "Booking deleted";
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking Booking) {
        deleteBooking(id);
        Booking updatedBooking = Booking.builder()
                .id(id)
                .flightId(Booking.getFlightId())
                .passengerId(Booking.getPassengerId())
                .cost(Booking.getCost())
                .build();
        Bookings.add(updatedBooking);
        return updatedBooking;
    }
}