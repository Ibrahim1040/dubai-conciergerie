package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.repository.BookingRepository;
import com.ibrahim.dubaiconciergerie.demo.repository.PropertyRepository;
import com.ibrahim.dubaiconciergerie.demo.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepo;
    private final PropertyRepository propertyRepo;

    public BookingServiceImpl(BookingRepository bookingRepo,
                              PropertyRepository propertyRepo) {
        this.bookingRepo = bookingRepo;
        this.propertyRepo = propertyRepo;
    }

    public Booking create(BookingDto dto) {
        Property property = propertyRepo.findById(dto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();

        if (start == null || end == null || end.isBefore(start)) {
            throw new RuntimeException("StartDate et EndDate invalides");
        }

        long conflicts = bookingRepo.countActiveBookingsInRange(
                property,
                start,
                end,
                Booking.Status.CANCELED
        );

        if (conflicts > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ce bien est déjà réservé sur cette période."
            );
        }

        Booking booking = Booking.builder()
                .property(property)
                .guestName(dto.getGuestName())
                .guestEmail(dto.getGuestEmail())
                .startDate(start)
                .endDate(end)
                .totalPrice(dto.getTotalPrice())
                .status(Booking.Status.PENDING)
                .build();

        return bookingRepo.save(booking);
    }


    @Override
    public List<Booking> getBookingsForProperty(Long propertyId) {
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        return bookingRepo.findByProperty(property);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getByProperty(Property property) {
        return bookingRepo.findByProperty(property);
    }

    @Override
    public Booking create(Booking booking) {
        return null;
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepo.findAll();
    }

    @Override
    public Booking getById(Long id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking introuvable"));
    }

    @Override
    public void delete(Long id) {
        if (!bookingRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Booking introuvable");
        }
        bookingRepo.deleteById(id);
    }

    @Override
    public List<Booking> getByProperty(Long propertyId) {
        return bookingRepo.findByPropertyIdOrderByStartDateAsc(propertyId);
    }

    @Override
    public void cancel(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(Booking.Status.CANCELED); // enum de ton entity
        bookingRepo.save(booking);
    }

    @Override
    public List<Booking> getByOwner(Long ownerId) {
        return bookingRepo.findByPropertyOwnerId(ownerId);
    }
}
