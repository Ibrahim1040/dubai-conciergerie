package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.repository.BookingRepository;
import com.ibrahim.dubaiconciergerie.demo.repository.PropertyRepository;
import com.ibrahim.dubaiconciergerie.demo.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public Booking create(BookingDto dto) {

        var property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();

        Booking booking = Booking.builder()
                .property(property)
                .guestName(dto.getGuestName())
                .guestEmail(dto.getGuestEmail())
                .startDate(start)
                .endDate(end)
                .totalPrice(dto.getTotalPrice())
                .status(Booking.Status.valueOf(dto.getStatus()))
                .build();


        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
