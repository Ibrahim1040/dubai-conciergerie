package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;

import java.util.List;

public final class BookingMapper {

    private BookingMapper() {}


        public static BookingResponseDto toResponseDto(Booking booking) {
            if (booking == null) return null;

            return BookingResponseDto.builder()
                    .id(booking.getId())
                    .propertyId(booking.getProperty().getId())
                    .guestName(booking.getGuestName())
                    .guestEmail(booking.getGuestEmail())
                    .startDate(booking.getStartDate())
                    .endDate(booking.getEndDate())
                    .status(String.valueOf(booking.getStatus()))
                    .totalPrice(booking.getTotalPrice())
                    .build();
        }

        public static List<BookingResponseDto> toResponseDtoList(List<Booking> bookings) {
            if (bookings == null) return List.of();
            return bookings.stream()
                    .map(BookingMapper::toResponseDto)
                    .toList();
        }

        public static Booking fromDto(BookingDto dto) {
            if (dto == null) return null;

            Booking booking = new Booking();
            booking.setGuestName(dto.getGuestName());
            booking.setGuestEmail(dto.getGuestEmail());
            booking.setStartDate(dto.getStartDate());
            booking.setEndDate(dto.getEndDate());
            // status et totalPrice seront calculés côté service
            return booking;
        }
    }

