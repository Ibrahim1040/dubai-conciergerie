package com.ibrahim.dubaiconciergerie.demo.controller;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.dto.BookingMapper;
import com.ibrahim.dubaiconciergerie.demo.dto.BookingResponseDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 1) Lister les réservations d’un bien (owner / admin)
    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Lister les réservations pour une propriété")
    public List<BookingResponseDto> getBookingsForProperty(@PathVariable Long propertyId) {
        List<Booking> bookings = bookingService.getBookingsForProperty(propertyId);
        return bookings.stream()
                .map(BookingMapper::toResponseDto)
                .toList();
    }

    // 2) Créer une réservation pour une propriété (guest via front)
    @PostMapping("/property/{propertyId}")
    @Operation(summary = "Créer une réservation pour une propriété")
    public BookingResponseDto createBooking(@PathVariable Long propertyId,
                                            @Valid @RequestBody BookingDto dto) {
        Booking booking = bookingService.createBooking(propertyId, dto);
        return BookingMapper.toResponseDto(booking);
    }

    // 3) Mettre à jour le statut (CONFIRMED / CANCELLED)
    @PatchMapping("/{bookingId}/status")
    @Operation(summary = "Changer le statut d’une réservation")
    public ResponseEntity<BookingResponseDto> updateStatus(@PathVariable Long bookingId,
                                                           @RequestParam Booking.Status status) {
        Booking updated = bookingService.updateStatus(bookingId, status);
        return ResponseEntity.ok(BookingMapper.toResponseDto(updated));
    }
}
