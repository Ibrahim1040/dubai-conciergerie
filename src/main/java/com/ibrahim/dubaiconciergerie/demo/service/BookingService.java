package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;

import java.util.List;

public interface BookingService {

    Booking create(Booking booking);

    List<Booking> getAll();

    BookingDto getById(Long id);

    void delete(Long id);

    List<Booking> getBookingsForProperty(Long propertyId);

    List<Booking> getByProperty(Property property);

    List<Booking> getByProperty(Long propertyId);

    void cancel(Long bookingId);

    List<Booking> getByOwner(Long ownerId);

    BookingDto update(Long id, BookingDto dto);


}
