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

        public static BookingDto toDto(Booking b) {
            if (b == null) return null;

            Long propertyId = (b.getProperty() != null) ? b.getProperty().getId() : null;
            String status = (b.getStatus() != null) ? b.getStatus().name() : null;

            return BookingDto.builder()
                    .id(b.getId())
                    .propertyId(propertyId)
                    .guestName(b.getGuestName())
                    .guestEmail(b.getGuestEmail())
                    .startDate(b.getStartDate())
                    .endDate(b.getEndDate())
                    .totalPrice(b.getTotalPrice())
                    .status(status)
                    .build();
        }
    }

