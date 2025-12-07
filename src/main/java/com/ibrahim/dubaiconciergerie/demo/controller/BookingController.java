package com.ibrahim.dubaiconciergerie.demo.controller;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.dto.BookingMapper;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.service.BookingService;
import com.ibrahim.dubaiconciergerie.demo.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    @Operation(summary = "Créer une réservation")
    public BookingDto createBooking(@RequestBody BookingDto dto) {

        Booking saved = bookingService.create(dto);
        return BookingMapper.toDto(saved);
    }


    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Lister les réservations d'une propriété")
    public List<BookingDto> getBookingsForProperty(@PathVariable Long propertyId) {
        Property property = propertyService.getById(propertyId);
        List<Booking> bookings = bookingService.getByProperty(property);
        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    @GetMapping
    @Operation(summary = "Lister toutes les réservations")
    public List<BookingDto> getBookings() {
        return bookingService.getAll().stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Annuler une réservation")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancel(id);
        return ResponseEntity.noContent().build(); // 204
    }

   /* // 👉 Nouveau : bookings d'une propriété
    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Lister les réservations d'une propriété")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public List<Booking> getByProperty(@PathVariable Long propertyId) {
        return bookingService.getByProperty(propertyId);
    }*/

    // 👉 Nouveau : bookings d'un owner
    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Lister les réservations des propriétés d'un owner")
    //@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public List<Booking> getByOwner(@PathVariable Long ownerId) {
        return bookingService.getByOwner(ownerId);
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
