package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.repository.BookingRepository;
import com.ibrahim.dubaiconciergerie.demo.repository.PropertyRepository;
import com.ibrahim.dubaiconciergerie.demo.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public Booking createBooking(Booking booking) {
        // petite validation logique : dates
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "startDate doit être avant endDate"
            );
        }

        long conflicts = bookingRepository.countActiveBookingsInRange(
                booking.getProperty(),
                booking.getStartDate(),
                booking.getEndDate(),
                Booking.Status.CANCELLED
        );

        if (conflicts > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ce bien est déjà réservé sur cette période."
            );
        }


        return bookingRepository.save(booking);
    }

    @Override
    public Booking createBooking(Long propertyId, BookingDto dto) {
        return null;
    }

    @Override
    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"
                ));
    }

    @Override
    public List<Booking> getBookingsForProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Property not found"
                ));
        return bookingRepository.findByProperty(property);
    }

    @Override
    public Booking cancelBooking(Long id) {
        Booking booking = getBooking(id);
        booking.setStatus(Booking.Status.CANCELLED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateStatus(Long bookingId, Booking.Status status) {
        return null;
    }
}
