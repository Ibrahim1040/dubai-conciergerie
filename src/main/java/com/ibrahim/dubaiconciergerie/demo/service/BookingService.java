package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking create(BookingDto dto);

    List<Booking> getAll();

    Booking getById(Long id);

    void delete(Long id);
}
