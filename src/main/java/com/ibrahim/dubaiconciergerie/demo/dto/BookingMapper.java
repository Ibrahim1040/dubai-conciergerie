package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;

public class BookingMapper {

    public static Booking toEntity(BookingDto dto, Property property) {
        return Booking.builder()
                .id(dto.getId())
                .property(property)
                .guestName(dto.getGuestName())
                .guestEmail(dto.getGuestEmail())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalPrice(dto.getTotalPrice())
                .status(Booking.Status.valueOf(dto.getStatus()))
                .build();
    }

    public static BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingDto.builder()
                .id(booking.getId())
                .propertyId(
                        booking.getProperty() != null
                                ? booking.getProperty().getId()
                                : null
                )
                .guestName(booking.getGuestName())
                .guestEmail(booking.getGuestEmail())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .totalPrice(booking.getTotalPrice())
                .status(
                        booking.getStatus() != null
                                ? booking.getStatus().name()
                                : null
                )
                .build();
    }
}
