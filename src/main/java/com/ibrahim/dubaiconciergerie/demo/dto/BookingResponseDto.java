package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BookingResponseDto {

    private Long id;

    // infos propriété
    private Long propertyId;
    private String propertyTitle;
    private String propertyCity;

    // dates de séjour
    private LocalDate startDate;
    private LocalDate endDate;

    // invité
    private String guestName;
    private String guestEmail;

    // statut + prix
    private String status;
    private Double totalPrice;
}

