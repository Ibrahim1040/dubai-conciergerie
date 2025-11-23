package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PropertyResponseDto {

    private Long id;
    private String title;
    private String city;
    private String address;
    private Integer capacity;
    private Property.RentalType  rentalType;

    private Double nightlyPrice;
    private Double monthlyPrice;

    // résumé du owner (sans password évidemment)
    private UserSummaryDto owner;
}

