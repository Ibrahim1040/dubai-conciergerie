package com.ibrahim.dubaiconciergerie.demo.controller;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.dto.BookingMapper;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.service.BookingService;
import com.ibrahim.dubaiconciergerie.demo.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/bookings")
@CrossOrigin("http://localhost:4200")
public class BookingController {

    private final BookingService bookingService;
    private final PropertyService propertyService;

    public BookingController(BookingService bookingService,
                             PropertyService propertyService) {
        this.bookingService = bookingService;
        this.propertyService = propertyService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto dto) {
        Booking saved = bookingService.create(dto);
        return ResponseEntity.status(201).body(BookingMapper.toDto(saved));
    }


    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Lister les réservations d'une propriété (paginé)")
    public Page<BookingDto> getBookingsForProperty(@PathVariable Long propertyId, Pageable pageable) {
        return bookingService.getByProperty(propertyId, pageable)
                .map(BookingMapper::toDto);
    }

    @GetMapping
    @Operation(summary = "Lister toutes les réservations")
    public Page<BookingDto> getBookings(Pageable pageable) {
        return bookingService.getAll(pageable)
                .map(BookingMapper::toDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Annuler une réservation")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancel(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // 👉 Nouveau : bookings d'un owner
    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Lister les réservations des propriétés d'un owner (paginé)")
    public Page<BookingDto> getByOwner(@PathVariable Long ownerId, Pageable pageable) {
        return bookingService.getByOwner(ownerId, pageable)
                .map(BookingMapper::toDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une réservation par son id")
    public BookingDto getBooking(@PathVariable Long id) {
        return bookingService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une réservation existante")
    public BookingDto updateBooking(@PathVariable Long id,
                                    @Valid @RequestBody BookingDto dto) {
        return bookingService.update(id, dto);
    }


}
