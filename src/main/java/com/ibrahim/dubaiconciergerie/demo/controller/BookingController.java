package com.ibrahim.dubaiconciergerie.demo.controller;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Operation(summary = "Créer une réservation")
    public Booking createBooking(@RequestBody BookingDto dto) {
        return bookingService.create(dto);
    }

    @GetMapping
    @Operation(summary = "Lister toutes les réservations")
    public List<Booking> getBookings() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une réservation par ID")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une réservation")
    public void delete(@PathVariable Long id) {
        bookingService.delete(id);
    }
}
