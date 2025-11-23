package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking);

    Booking createBooking(Long propertyId, BookingDto dto);

    Booking getBooking(Long id);

    List<Booking> getBookingsForProperty(Long propertyId);

    Booking cancelBooking(Long id);

    Booking updateStatus(Long bookingId, Booking.Status status);
}
