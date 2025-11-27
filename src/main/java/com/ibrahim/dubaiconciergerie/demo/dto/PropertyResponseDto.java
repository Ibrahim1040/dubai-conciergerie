package com.ibrahim.dubaiconciergerie.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyResponseDto {
    private Long id;
    private String title;
    private String city;
    private String address;
    private int capacity;
    private String rentalType;
    private double nightlyPrice;
    private double monthlyPrice;
    private Long ownerId; // toujours utile !
}
